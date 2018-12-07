package com.fungo.sample.data.api

import com.fungo.netgo.RxNetGo

/**
 * @author Pinger
 * @since 18-12-7 上午9:42
 */
interface BaseApi {

    /**
     * 获取RxNetGo引用
     */
    fun getRxNetGo(): RxNetGo


    /**
     * 生成RxNetGo默认的Service
     */
    fun generateService(): RxNetGo


    /**
     * 对url获取某些参数进行签名
     */
    fun sign(url: String): String
}