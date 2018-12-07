package com.fungo.baselib.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.util.DisplayMetrics
import java.text.DecimalFormat

/**
 * @author Pinger
 * @since 18-6-28 下午7:11
 * 今日头条屏幕适配方案
 * [https://juejin.im/post/5b3094fc6fb9a00e52398ae4?utm_source=gold_browser_extension]
 * [https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA]
 */

object LayoutUtils {

    private var appDensity: Float = 0f
    private var appScaledDensity: Float = 0f
    private var appDisplayMetrics: DisplayMetrics? = null

    private var LAYOUT_HEIGHT = 1920 / 3
    private var LAYOUT_WIDTH = 1080 / 3

    var STANDARD_WIDTH = 1  // 以宽为标准计算适配
    var STANDARD_HEIGHT = 2  // 以高为标准计算适配

    /**
     * 设置application的适配方案，默认适配的宽高为1080和1920
     * @param application 全局对象
     */
    fun initLayout(application: Application) {
        initLayout(application, LAYOUT_WIDTH, LAYOUT_HEIGHT)
    }

    /**
     * 设置application的适配方案，适配的宽高需要自定义
     * @param application 全局对象
     * @param width 宽
     * @param height 高
     */
    fun initLayout(application: Application, width: Int, height: Int) {
        LAYOUT_WIDTH = width
        LAYOUT_HEIGHT = height

        // 获取application的DisplayMetrics
        appDisplayMetrics = application.resources.displayMetrics

        if (appDensity == 0f) {
            // 初始化的时候赋值（只在Application里面初始化的时候会调用一次）
            appDensity = appDisplayMetrics?.density ?: 0f
            appScaledDensity = appDisplayMetrics?.scaledDensity ?: 0f

            // 添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }
            })
        }

        // 调用修改density值的方法(默认以宽度作为基准)
        setLayout(null, STANDARD_WIDTH)
    }


    /**
     * 设置适配方案
     * @param activity 适配的Activity对象，所有该Activity的子View都将会被是配到
     * @param standard 适配的标准，默认以宽为标准适配
     */
    fun setLayout(activity: Activity?, standard: Int) {

        var targetDensity: Float
        try {
            val divisionValue: Double = if (standard == STANDARD_WIDTH) {
                division(appDisplayMetrics?.widthPixels ?: 0, LAYOUT_WIDTH)
            } else {
                division(appDisplayMetrics?.heightPixels ?: 0, LAYOUT_HEIGHT)
            }
            // 根据带入参数选择不同的适配方向
            // 由于手机的长宽不尽相同,肯定会有除不尽的情况,有失精度,所以在这里把所得结果做了一个保留两位小数的操作
            targetDensity = DecimalFormat("0.00").format(divisionValue).toFloat()
        } catch (e: NumberFormatException) {
            targetDensity = (appDisplayMetrics?.widthPixels ?: 0 / 360).toFloat()
            e.printStackTrace()
        }

        val targetScaledDensity: Float = targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi: Int = (160 * targetDensity).toInt()

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * (因为最开始初始化的时候,activity为null,所以只设置application的值就可以了...
         * 所以在这里判断了一下,如果传有activity的话,再设置Activity的值)
         */
        if (activity != null) {
            val activityDisplayMetrics = activity.resources.displayMetrics
            activityDisplayMetrics.density = targetDensity
            activityDisplayMetrics.scaledDensity = targetScaledDensity
            activityDisplayMetrics.densityDpi = targetDensityDpi
        } else {
            appDisplayMetrics?.density = targetDensity
            appDisplayMetrics?.scaledDensity = targetScaledDensity
            appDisplayMetrics?.densityDpi = targetDensityDpi
        }
    }


    /**
     * 除法
     * @param a 分子
     * @param b 分母
     * @return 返回最终值，Double对象
     */
    private fun division(a: Int, b: Int): Double {
        return if (b != 0) {
            a.toDouble() / b.toDouble()
        } else {
            0.00
        }
    }
}