package com.fungo.netgo.subscribe.base

import com.fungo.netgo.convert.base.IConverter
import com.fungo.netgo.exception.ApiException

/**
 * @author Pinger
 * @since 18-11-30 下午6:04
 *
 * 定义订阅者的回调方法，方便处理结果
 */
interface ISubscriber<T> : IConverter<T> {

    /**
     * 请求成功，[data]必不为空，如果为空的话会回调[onError]方法
     */
    fun onSuccess(data: T)

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    fun onError(exception: ApiException)
}