package com.fungo.netgo.subscribe

/**
 * @author Pinger
 * @since 18-10-19 上午11:57
 *
 * 默认没有订阅结果的订阅者
 * 当不需要关注请求结果时，使用本订阅者去处理请求过程中发生的异常。
 *
 */
class DefalutSubscriber : StringSubscriber() {

    override fun onSuccess(data: String) {
    }
}
