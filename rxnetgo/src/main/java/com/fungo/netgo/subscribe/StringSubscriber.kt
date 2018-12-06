package com.fungo.netgo.subscribe

import com.fungo.netgo.convert.StringConvert
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-10-19 上午11:41
 *
 *
 * 当请求返回的结果为String类型时，可以使用本订阅者，会handle请求过程中的异常。
 */
abstract class StringSubscriber : BaseSubscriber<String>() {

    @Throws(Exception::class)
    final override fun convertResponse(body: ResponseBody?): String? {
        return StringConvert().convertResponse(body)
    }

    final override fun getType(): Type {
        return String::class.java
    }
}
