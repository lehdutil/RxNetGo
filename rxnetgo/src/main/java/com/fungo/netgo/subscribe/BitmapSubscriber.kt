package com.fungo.netgo.subscribe

import android.graphics.Bitmap
import com.fungo.netgo.convert.BitmapConvert
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-12-3 下午3:51
 *
 * 图片订阅者，生成bitmap对象
 */
abstract class BitmapSubscriber : BaseSubscriber<Bitmap?>() {

    private val mConvert: BitmapConvert = BitmapConvert()

    override fun onComplete() {
    }

    override fun onError(exception: ApiException) {
    }

    override fun convertResponse(response: ResponseBody?): Bitmap? {
        return mConvert.convertResponse(response)
    }
}