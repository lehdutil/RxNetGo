package com.fungo.sample.data

import android.content.Context
import java.io.InputStream

/**
 * @author Pinger
 * @since 2018/12/12 11:27
 *
 * 静态数据提供者
 */
object DataProvider {

    /**
     * 获取mipmap中的开屏图片
     */
    fun getSplashData(context: Context): List<InputStream> {
        val data = arrayListOf<InputStream>()
//        data.add("file:///android_asset/bg_splash_01.jpg")
//        data.add("file:///android_asset/bg_splash_02.jpg")
//        data.add("file:///android_asset/bg_splash_03.jpg")
//        data.add("file:///android_asset/bg_splash_04.jpg")
        data.add(context.assets.open("bg_splash_01.png"))
        return data
    }
}