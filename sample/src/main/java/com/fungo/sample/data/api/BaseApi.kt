package com.fungo.sample.data.api

import com.fungo.netgo.RxNetGo
import com.fungo.netgo.model.HttpParams

/**
 * @author Pinger
 * @since 18-12-7 上午9:42
 */
interface BaseApi {


    fun getBaseUrl(): String

    /**
     * 获取RxNetGo引用
     */
    fun getRxNetGo(): RxNetGo = RxNetGo.getInstance()

    /**
     * 生成RxNetGo默认的Service
     */
    fun generateService(): RxNetGo = getRxNetGo().getRetrofitService(getBaseUrl())


    /**
     * 对url获取某些参数进行签名
     */
    fun sign(params: HttpParams): String = params.toString()
}