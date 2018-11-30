package com.fungo.netgo.subscribe.base

import com.fungo.netgo.exception.ApiException

/**
 * @author Pinger
 * @since 18-11-30 下午6:04
 */
interface ISubscriber<T> : IConverter<T> {

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    fun onError(exception: ApiException)
}