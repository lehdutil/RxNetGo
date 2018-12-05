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
     */
    @Throws(Exception::class)
    fun convertResponse(response: ResponseBody?): T
}
