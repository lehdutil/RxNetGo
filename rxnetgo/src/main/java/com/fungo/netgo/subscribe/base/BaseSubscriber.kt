package com.fungo.netgo.subscribe.base

import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.exception.NetErrorEngine
import java.lang.reflect.Type

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
     * 请求开始
     */
    override fun onStart() {
    }

    /**
     * 请求成功
     */
    abstract override fun onSuccess(data: T)

    /**
     * 请求异常
     */
    override fun onError(exception: ApiException) {}


    /**
     * 请求完成
     */
    open fun onCompleted() {}

    /**
     * 返回泛型类型
     */
    abstract fun getType(): Type

    /**
     * 转发请求成功结果
     */
    final override fun onNext(data: T) {
        this.dispose()
        onSuccess(data)
    }

    /**
     * 转发请求失败发生的异常
     * 发生异常自动取消订阅，如果要做重连，需要在onErrorResumeNext时重新请求
     */
    final override fun onError(e: Throwable) {
        this.dispose()
        if (e is ApiException) {
            onError(e)
        } else {
            onError(ApiException(error = e, code = NetErrorEngine.UNKNOWN_CODE, msg = NetErrorEngine.UNKNOW_MSG))
        }
    }


    /**
     * 请求完成
     * 请求完成了自动取消订阅，所有的请求都处理
     */
    final override fun onComplete() {
        this.dispose()
        onCompleted()
    }

}
