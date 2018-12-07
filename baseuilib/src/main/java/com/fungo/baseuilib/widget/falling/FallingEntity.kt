package com.fungo.baseuilib.widget.falling

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import java.util.*

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:18
 *
 * 下路对象实体，控制相关属性
 * setSpeed(int speed) ：设置物体的初始下落速度
 * setSpeed(int speed,boolean isRandomSpeed) ： 设置物体的初始下落速度，isRandomSpeed：物体初始下降速度比例是否随机
 * setSize(int w, int h) ： 设置物体大小
 * setSize(int w, int h, boolean isRandomSize) ： 设置物体大小，isRandomSize：物体初始大小比例是否随机
 * setWind(int level,boolean isWindRandom,boolean isWindChange) ： 设置风力等级、方向以及随机因素，level：风力等级，isWindRandom：物体初始风向和风力大小比例是否随机，isWindChange：在物体下落过程中风的风向和风力是否会产生随机变化
 */

class FallingEntity {

    companion object {
        const val defaultSpeed = 10            // 默认下降速度
        const val defaultWindLevel = 0         // 默认风力等级
        const val defaultWindSpeed = 10        // 默认单位风速
        const val HALF_PI = Math.PI.toFloat() / 2 // π/2


        /**
         * drawable图片资源转bitmap
         *
         * @param drawable
         * @return
         */
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    if (drawable.opacity != PixelFormat.OPAQUE)
                        Bitmap.Config.ARGB_8888
                    else
                        Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        }

        /**
         * 改变bitmap的大小
         *
         * @param bitmap 目标bitmap
         * @param newW   目标宽度
         * @param newH   目标高度
         * @return
         */
        fun changeBitmapSize(bitmap: Bitmap?, newW: Int, newH: Int): Bitmap? {
            var newBitmap = bitmap
            val oldW = newBitmap!!.width
            val oldH = newBitmap.height
            // 计算缩放比例
            val scaleWidth = newW.toFloat() / oldW
            val scaleHeight = newH.toFloat() / oldH
            // 取得想要缩放的matrix参数
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            // 得到新的图片
            newBitmap = Bitmap.createBitmap(newBitmap, 0, 0, oldW, oldH, matrix, true)
            return newBitmap
        }
    }

    private var initX: Int = 0
    private var initY: Int = 0
    private val random: Random = Random()
    private var parentWidth: Int = 0    // 父容器宽度
    private var parentHeight: Int = 0   // 父容器高度
    private var objectWidth: Float = 0.toFloat()   // 下落物体宽度
    private var objectHeight: Float = 0.toFloat()  // 下落物体高度


    private var initSpeed: Int = 0       // 初始下降速度
    private var initWindLevel: Int = 0   // 初始风力等级

    private var presentX: Float = 0.toFloat()      // 当前位置X坐标
    private var presentY: Float = 0.toFloat()      // 当前位置Y坐标
    private var presentSpeed: Float = 0.toFloat()  // 当前下降速度
    private var angle: Float = 0.toFloat()         // 物体下落角度

    private var bitmap: Bitmap? = null
    private var builder: Builder

    private var isSpeedRandom: Boolean = false // 物体初始下降速度比例是否随机
    private var isSizeRandom: Boolean = false  // 物体初始大小比例是否随机
    private var isWindRandom: Boolean = false  // 物体初始风向和风力大小比例是否随机
    private var isWindChange: Boolean = false  // 物体下落过程中风向和风力是否产生随机变化

    constructor(builder: Builder, parentWidth: Int, parentHeight: Int) {
        this.parentWidth = parentWidth
        this.parentHeight = parentHeight
        initX = random.nextInt(parentWidth)
        initY = random.nextInt(parentHeight) - parentHeight
        presentX = initX.toFloat()
        presentY = initY.toFloat()

        this.builder = builder
        isSpeedRandom = builder.isSpeedRandom
        isSizeRandom = builder.isSizeRandom
        isWindRandom = builder.isWindRandom
        isWindChange = builder.isWindChange

        initSpeed = builder.initSpeed
        randomSpeed()
        randomSize()
        randomWind()
    }

    private constructor(builder: Builder) {
        this.builder = builder
        initSpeed = builder.initSpeed
        bitmap = builder.bitmap

        isSpeedRandom = builder.isSpeedRandom
        isSizeRandom = builder.isSizeRandom
        isWindRandom = builder.isWindRandom
        isWindChange = builder.isWindChange
    }

    class Builder {
        var initSpeed: Int = 0
        var initWindLevel: Int = 0
        var bitmap: Bitmap? = null

        var isSpeedRandom: Boolean = false
        var isSizeRandom: Boolean = false
        var isWindRandom: Boolean = false
        var isWindChange: Boolean = false

        constructor(bitmap: Bitmap) {
            this.initSpeed = defaultSpeed
            this.initWindLevel = defaultWindLevel
            this.bitmap = bitmap

            this.isSpeedRandom = false
            this.isSizeRandom = false
            this.isWindRandom = false
            this.isWindChange = false
        }

        constructor(drawable: Drawable) {
            this.initSpeed = defaultSpeed
            this.initWindLevel = defaultWindLevel
            this.bitmap = drawableToBitmap(drawable)

            this.isSpeedRandom = false
            this.isSizeRandom = false
            this.isWindRandom = false
            this.isWindChange = false
        }

        /**
         * 设置物体的初始下落速度
         *
         * @param speed
         * @return
         */
        fun setSpeed(speed: Int): Builder {
            this.initSpeed = speed
            return this
        }

        /**
         * 设置物体的初始下落速度
         *
         * @param speed
         * @param isRandomSpeed 物体初始下降速度比例是否随机
         * @return
         */
        fun setSpeed(speed: Int, isRandomSpeed: Boolean): Builder {
            this.initSpeed = speed
            this.isSpeedRandom = isRandomSpeed
            return this
        }

        /**
         * 设置物体大小
         *
         * @param w
         * @param h
         * @return
         */
        fun setSize(w: Int, h: Int): Builder {
            this.bitmap = changeBitmapSize(this.bitmap, w, h)
            return this
        }

        /**
         * 设置物体大小
         *
         * @param w
         * @param h
         * @param isRandomSize 物体初始大小比例是否随机
         * @return
         */
        fun setSize(w: Int, h: Int, isRandomSize: Boolean): Builder {
            this.bitmap = changeBitmapSize(this.bitmap, w, h)
            this.isSizeRandom = isRandomSize
            return this
        }

        /**
         * 设置风力等级、方向以及随机因素
         *
         * @param level        风力等级（绝对值为 5 时效果会比较好），为正时风从左向右吹（物体向X轴正方向偏移），为负时则相反
         * @param isWindRandom 物体初始风向和风力大小比例是否随机
         * @param isWindChange 在物体下落过程中风的风向和风力是否会产生随机变化
         * @return
         */
        fun setWind(level: Int, isWindRandom: Boolean, isWindChange: Boolean): Builder {
            this.initWindLevel = level
            this.isWindRandom = isWindRandom
            this.isWindChange = isWindChange
            return this
        }

        fun build(): FallingEntity {
            return FallingEntity(this)
        }
    }

    /**
     * 绘制物体对象
     *
     * @param canvas
     */
    fun drawObject(canvas: Canvas) {
        moveObject()
        canvas.drawBitmap(bitmap!!, presentX, presentY, null)
    }

    /**
     * 移动物体对象
     */
    private fun moveObject() {
        moveX()
        moveY()
        if (presentY > parentHeight || presentX < -bitmap!!.width || presentX > parentWidth + bitmap!!.width) {
            reset()
        }
    }

    /**
     * X轴上的移动逻辑
     */
    private fun moveX() {
        presentX += (defaultWindSpeed * Math.sin(angle.toDouble())).toFloat()
        if (isWindChange) {
            angle += ((if (random.nextBoolean()) -1 else 1).toFloat().toDouble() * Math.random() * 0.0025).toFloat()
        }
    }

    /**
     * Y轴上的移动逻辑
     */
    private fun moveY() {
        presentY += presentSpeed
    }

    /**
     * 重置object位置
     */
    private fun reset() {
        presentY = -objectHeight
        randomSpeed() // 记得重置时速度也一起重置，这样效果会好很多
        randomWind()  // 记得重置一下初始角度，不然雪花会越下越少（因为角度累加会让雪花越下越偏）
    }

    /**
     * 随机物体初始下落速度
     */
    private fun randomSpeed() {
        presentSpeed = if (isSpeedRandom) {
            ((random.nextInt(3) + 1) * 0.1 + 1).toFloat() * initSpeed//这些随机数大家可以按自己的需要进行调整
        } else {
            initSpeed.toFloat()
        }
    }

    /**
     * 随机物体初始大小比例
     */
    private fun randomSize() {
        bitmap = if (isSizeRandom) {
            val r = (random.nextInt(10) + 1) * 0.1f
            val rW = r * builder.bitmap!!.width
            val rH = r * builder.bitmap!!.height
            changeBitmapSize(builder.bitmap, rW.toInt(), rH.toInt())
        } else {
            builder.bitmap
        }
        objectWidth = bitmap!!.width.toFloat()
        objectHeight = bitmap!!.height.toFloat()
    }

    /**
     * 随机风的风向和风力大小比例，即随机物体初始下落角度
     */
    private fun randomWind() {
        angle = if (isWindRandom) {
            ((if (random.nextBoolean()) -1 else 1).toDouble() * Math.random() * initWindLevel.toDouble() / 50).toFloat()
        } else {
            initWindLevel.toFloat() / 50
        }

        // 限制angle的最大最小值
        if (angle > HALF_PI) {
            angle = HALF_PI
        } else if (angle < -HALF_PI) {
            angle = -HALF_PI
        }
    }


    fun getBuilder(): Builder {
        return builder
    }


}
