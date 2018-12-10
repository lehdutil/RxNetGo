package com.fungo.baseuilib.recycler.header.material

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getSize
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import com.fungo.baseuilib.R
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.util.DensityUtil

/**
 * 谷歌下拉刷新头
 */
class GoogleMaterialHeader : ViewGroup, RefreshHeader {

    private var mFinished: Boolean = false
    private var mCircleDiameter: Int = 0
    private lateinit var mCircleView: CircleImageView
    private lateinit var mProgress: MaterialProgressDrawable

    /**
     * 贝塞尔背景
     */
    private lateinit var mBezierPath: Path
    private lateinit var mBezierPaint: Paint
    private var mWaveHeight: Int = 0
    private var mHeadHeight: Int = 0
    private var mShowBezierWave = false
    private var mState: RefreshState? = null

    //<editor-fold desc="MaterialHeader">
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        minimumHeight = DensityUtil.dp2px(100f)

        mProgress = MaterialProgressDrawable(this)
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT)
        mProgress.alpha = 255
        mProgress.setColorSchemeColors(getThemeColor(context))
        mCircleView = CircleImageView(context, CIRCLE_BG_LIGHT)
        mCircleView.setImageDrawable(mProgress)
        mCircleView.visibility = View.GONE
        addView(mCircleView)

        val metrics = resources.displayMetrics
        mCircleDiameter = (CIRCLE_DIAMETER * metrics.density).toInt()

        mBezierPath = Path()
        mBezierPaint = Paint()
        mBezierPaint.isAntiAlias = true
        mBezierPaint.style = Paint.Style.FILL

        val ta = context.obtainStyledAttributes(attrs, R.styleable.GoogleMaterialHeader)
        mShowBezierWave = ta.getBoolean(R.styleable.GoogleMaterialHeader_gmhShowBezierWave, mShowBezierWave)
        mBezierPaint.color = ta.getColor(R.styleable.GoogleMaterialHeader_gmhPrimaryColor, getThemeColor(context))
        if (ta.hasValue(R.styleable.GoogleMaterialHeader_gmhShadowRadius)) {
            val radius = ta.getDimensionPixelOffset(R.styleable.GoogleMaterialHeader_gmhShadowRadius, 0)
            val color = ta.getColor(R.styleable.GoogleMaterialHeader_gmhShadowColor, -0x1000000)
            mBezierPaint.setShadowLayer(radius.toFloat(), 0f, 0f, color)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        ta.recycle()

    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec))
        mCircleView.measure(View.MeasureSpec.makeMeasureSpec(mCircleDiameter, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(mCircleDiameter, View.MeasureSpec.EXACTLY))
        //        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec),
        //                resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount == 0) {
            return
        }
        val width = measuredWidth
        val circleWidth = mCircleView.measuredWidth
        val circleHeight = mCircleView.measuredHeight

        if (isInEditMode && mHeadHeight > 0) {
            val circleTop = mHeadHeight - circleHeight / 2
            mCircleView.layout(width / 2 - circleWidth / 2, circleTop,
                    width / 2 + circleWidth / 2, circleTop + circleHeight)

            mProgress.showArrow(true)
            mProgress.setStartEndTrim(0f, MAX_PROGRESS_ANGLE)
            mProgress.setArrowScale(1f)
            mCircleView.alpha = 1f
            mCircleView.visibility = View.VISIBLE
        } else {
            mCircleView.layout(width / 2 - circleWidth / 2, -mCircleDiameter,
                    width / 2 + circleWidth / 2, circleHeight - mCircleDiameter)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (mShowBezierWave) {
            //重置画笔
            mBezierPath.reset()
            mBezierPath.lineTo(0f, mHeadHeight.toFloat())
            //绘制贝塞尔曲线
            mBezierPath.quadTo((measuredWidth / 2).toFloat(), mHeadHeight + mWaveHeight * 1.9f, measuredWidth.toFloat(), mHeadHeight.toFloat())
            mBezierPath.lineTo(measuredWidth.toFloat(), 0f)
            canvas.drawPath(mBezierPath, mBezierPaint)
        }
        super.dispatchDraw(canvas)
    }

    //</editor-fold>

    //<editor-fold desc="API">

    /**
     * One of DEFAULT, or LARGE.
     */
    fun setSize(size: Int): GoogleMaterialHeader {
        if (size != SIZE_LARGE && size != SIZE_DEFAULT) {
            return this
        }
        val metrics = resources.displayMetrics
        mCircleDiameter = if (size == SIZE_LARGE) {
            (CIRCLE_DIAMETER_LARGE * metrics.density).toInt()
        } else {
            (CIRCLE_DIAMETER * metrics.density).toInt()
        }
        // force the bounds of the progress circle inside the circle view to
        // update by setting it to null before updating its size and then
        // re-setting it
        mCircleView.setImageDrawable(null)
        mProgress.updateSizes(size)
        mCircleView.setImageDrawable(mProgress)
        return this
    }

    fun setShowBezierWave(show: Boolean): GoogleMaterialHeader {
        this.mShowBezierWave = show
        return this
    }

    //</editor-fold>


    //<editor-fold desc="RefreshHeader">
    override fun onInitialized(kernel: RefreshKernel, height: Int, extendHeight: Int) {
        if (!mShowBezierWave) {
            kernel.requestDefaultHeaderTranslationContent(false)
        }
        if (isInEditMode) {
            mHeadHeight = height / 2
            mWaveHeight = mHeadHeight
        }
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}

    override fun onPullingDown(percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
        if (mShowBezierWave) {
            mHeadHeight = Math.min(offset, headerHeight)
            mWaveHeight = Math.max(0, offset - headerHeight)
            postInvalidate()
        }

        if (mState != RefreshState.Refreshing) {
            val originalDragPercent = 1f * offset / headerHeight

            val dragPercent = Math.min(1f, Math.abs(originalDragPercent))
            val adjustedPercent = Math.max(dragPercent - .4, 0.0).toFloat() * 5 / 3
            val extraOS = (Math.abs(offset) - headerHeight).toFloat()
            val tensionSlingshotPercent = Math.max(0f, Math.min(extraOS, headerHeight.toFloat() * 2) / headerHeight.toFloat())
            val tensionPercent = (tensionSlingshotPercent / 4 - Math.pow(
                    (tensionSlingshotPercent / 4).toDouble(), 2.0)).toFloat() * 2f
            val strokeStart = adjustedPercent * .8f
            mProgress.showArrow(true)
            mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart))
            mProgress.setArrowScale(Math.min(1f, adjustedPercent))

            val rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f
            mProgress.setProgressRotation(rotation)
            mCircleView.alpha = Math.min(1f, originalDragPercent * 2)
        }

        val targetY = (offset / 2 + mCircleDiameter / 2).toFloat()
        mCircleView.translationY = Math.min(offset.toFloat(), targetY)//setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true /* requires update */);
    }

    override fun onReleasing(percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {
        if (!mProgress.isRunning && !mFinished) {
            onPullingDown(percent, offset, headerHeight, extendHeight)
        } else {
            if (mShowBezierWave) {
                mHeadHeight = Math.min(offset, headerHeight)
                mWaveHeight = Math.max(0, offset - headerHeight)
                postInvalidate()
            }
        }
    }

    override fun onRefreshReleased(layout: RefreshLayout, headerHeight: Int, extendHeight: Int) {
        mProgress.start()
        if (mCircleView.translationY.toInt() != headerHeight / 2 + mCircleDiameter / 2) {
            mCircleView.animate()?.translationY((headerHeight / 2 + mCircleDiameter / 2).toFloat())
        }
    }

    override fun onStartAnimator(layout: RefreshLayout, headerHeight: Int, extendHeight: Int) {

    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        mState = newState
        when (newState) {
            RefreshState.None -> {
            }
            RefreshState.PullDownToRefresh -> {
                mFinished = false
                mCircleView.visibility = View.VISIBLE
                mCircleView.scaleX = 1f
                mCircleView.scaleY = 1f
            }
            RefreshState.ReleaseToRefresh -> {
            }
            RefreshState.Refreshing -> {
            }
            else -> {
            }
        }
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        mProgress.stop()
        mCircleView.animate()?.scaleX(0f)?.scaleY(0f)
        mFinished = true
        return 0
    }

    @Deprecated("")
    override fun setPrimaryColors(@ColorInt vararg colors: Int) {
        if (colors.isNotEmpty()) {
            mBezierPaint.color = colors[0]
        }
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.MatchLayout
    }
    //</editor-fold>


    //<editor-fold desc="API">
    fun setColorSchemeColors(vararg colors: Int): GoogleMaterialHeader {
        mProgress.setColorSchemeColors(*colors)
        return this
    }
    //</editor-fold>

    private fun getThemeColor(context: Context): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }

    companion object {

        // Maps to ProgressBar.Large style
        const val SIZE_LARGE = 0
        // Maps to ProgressBar default style
        const val SIZE_DEFAULT = 1

        private const val CIRCLE_BG_LIGHT = -0x50506
        private const val MAX_PROGRESS_ANGLE = .8f
        @VisibleForTesting
        private val CIRCLE_DIAMETER = 40
        @VisibleForTesting
        private val CIRCLE_DIAMETER_LARGE = 56
    }


}
