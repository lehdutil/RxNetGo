package com.fungo.netgo.convert.base

import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-10-23 上午11:29
 *
 *
 * 自定义转换器，提供给子类转换数据格式
 * 如果使用自定义的Retrofit的Service，则不需要转换数据
 */
interface IConverter<T> {

    /**
     * 将服务器返回的Response，抓换成想要的数据实体
     * 解析回来的数据有可能会是空的，因为如果类型没匹配上，或者发生了被捕获的异常
     *
     * 如果返回的T是null的话，则会作为异常返回
     */
    @Throws(Exception::class)
    fun convertResponse(response: ResponseBody?): T?
}
