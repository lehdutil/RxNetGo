package com.fungo.netgo.subscribe

import com.fungo.netgo.convert.StringConvert
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-10-19 上午11:41
 *
 *
 * 当请求返回的结果为String类型时，可以使用本订阅者，会handle请求过程中的异常。
 */
open class StringSubscriber : BaseSubscriber<String>() {

    private val mConverter: StringConvert = StringConvert()

    override fun onStart() {

    }

    override fun onNext(json: String) {

    }

    override fun onError(exception: ApiException) {

    }


    override fun onComplete() {

    }

    @Throws(Exception::class)
    final override fun convertResponse(response: ResponseBody?): String {
        return mConverter.convertResponse(response)
    }
}
