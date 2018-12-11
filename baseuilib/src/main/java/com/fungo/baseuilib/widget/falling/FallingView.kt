package com.fungo.baseuilib.widget.falling

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:18
 * 雪花、雨滴、金币等满屏飘落效果实现
 * [{]
 */

class FallingView : View {

    private var mFallingEntities = arrayListOf<FallingEntity>()

    private val intervalTime = 10       //重绘间隔时间

    // 重绘线程
    private val runnable = Runnable { invalidate() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)



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
        if (mFallingEntities.size > 0) {
            for (i in mFallingEntities.indices) {
                // 然后进行绘制
                mFallingEntities[i].drawObject(canvas)
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
                    mFallingEntities.add(FallingEntity(fallingEntity.getBuilder(), measuredWidth, measuredHeight))
                }
                invalidate()
                return true
            }
        })
    }

    fun release() {
        handler.removeCallbacks(null)
    }
}
