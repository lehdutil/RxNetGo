package com.fungo.netgo.subscribe

import android.graphics.Bitmap
import com.fungo.netgo.convert.BitmapConvert
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:51
 *
 * 图片订阅者，生成bitmap对象
 */
abstract class BitmapSubscriber : BaseSubscriber<Bitmap?>() {

    override fun convertResponse(response: ResponseBody?): Bitmap? {
        return BitmapConvert().convertResponse(response)
    }

    override fun getType(): Type {
        return Bitmap::class.java
    }
}