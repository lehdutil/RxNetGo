package com.fungo.netgo.lifecycle

import org.reactivestreams.Subscription

/**
 * @author Pinger
 * @since 18-12-4 下午6:41
 *
 * Rx请求动作接口，对每一个请求添加tag，方便管理生命周期
 */
interface IRequestAction {

    /**
     * 添加一个请求
     */
    fun add(tag: Any, subscription: Subscription)

    /**
     * 移除指定请求
     */
    fun remove(tag: Any)

    /**
     * 取消指定请求
     */
    fun cancel(tag: Any)

    /**
     * 取消所有请求
     */
    fun cancelAll()

}