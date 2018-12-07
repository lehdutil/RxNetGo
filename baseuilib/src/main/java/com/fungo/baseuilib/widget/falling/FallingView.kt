package com.fungo.baseuilib.widget.falling

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import java.util.*

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:18
 * 雪花、雨滴、金币等满屏飘落效果实现
 * [{]
 */

class FallingView : View {


    private var mFallingEntities: MutableList<FallingEntity>? = null

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private val defaultWidth = 600     //默认宽度
    private val defaultHeight = 1000   //默认高度
    private val intervalTime = 5       //重绘间隔时间


    // 重绘线程
    private val runnable = Runnable { invalidate() }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        mFallingEntities = ArrayList()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measureSize(defaultHeight, heightMeasureSpec)
        val width = measureSize(defaultWidth, widthMeasureSpec)
        setMeasuredDimension(width, height)

        viewWidth = width
        viewHeight = height
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mFallingEntities!!.size > 0) {
            for (i in mFallingEntities!!.indices) {
                // 然后进行绘制
                mFallingEntities!![i].drawObject(canvas)
            }
            // 隔一段时间重绘一次, 动画效果
            handler.postDelayed(runnable, intervalTime.toLong())
        }
    }

    /**
     * 向View添加下落物体对象
     *
     * @param fallingEntity 下落物体对象
     * @param num
     */
    fun addFallEntity(fallingEntity: FallingEntity, num: Int) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                for (i in 0 until num) {
                    val newFallEntity = FallingEntity(fallingEntity.builder, viewWidth, viewHeight)
                    mFallingEntities!!.add(newFallEntity)
                }
                invalidate()
                return true
            }
        })
    }
}
