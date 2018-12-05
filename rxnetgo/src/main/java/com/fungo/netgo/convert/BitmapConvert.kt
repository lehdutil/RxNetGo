package com.fungo.netgo.convert

import android.graphics.Bitmap
import com.fungo.netgo.convert.base.IConverter
import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-12-3 下午3:47
 *
 * TODO
 * 图片流转换器，返回的是一张图片的流，将流转换成图片
 */
class BitmapConvert : IConverter<Bitmap?> {

    override fun convertResponse(response: ResponseBody?): Bitmap? {
        return null
    }
}