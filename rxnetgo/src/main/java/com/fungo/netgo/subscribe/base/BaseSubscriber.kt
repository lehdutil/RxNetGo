package com.fungo.netgo.subscribe.base

import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.exception.NetError

/**
 * @author Pinger
 * @since 18-10-19 上午11:08
 *
 * 订阅者基类，抽离出来，应对其他的变化
 *
 * 可以自定义订阅者，实现本基类，自定义数据结构
 * 不同的数据结构可以实现不同的订阅者，这样就可以处理不同的数据了
 *
 */
abstract class BaseSubscriber<T> : ResourceSubscriber<T>(), ISubscriber<T> {


    /**
     * 分发一下异常
     */
    final override fun onError(e: Throwable) {
        if (e is ApiException) {
            onError(e)
        } else {
            onError(ApiException(e, NetError.UNKNOWN))
        }
    }
}