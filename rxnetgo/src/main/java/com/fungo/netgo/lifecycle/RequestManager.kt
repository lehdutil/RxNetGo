package com.fungo.netgo.lifecycle

import org.reactivestreams.Subscription

/**
 * @author Pinger
 * @since 18-12-4 下午6:44
 *
 * 管理请求的添加和移除，管理网络请求的生命周期
 *
 */
class RequestManager : IRequestAction {


    private val mRequests: Map<Any, Subscription> = HashMap()


    override fun add(tag: Any, subscription: Subscription) {
    }

    override fun remove(tag: Any) {
    }

    override fun cancel(tag: Any) {
    }

    override fun cancelAll() {
    }


}