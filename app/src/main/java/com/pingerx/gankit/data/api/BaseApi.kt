package com.pingerx.gankit.data.api

import com.pingerx.rxnetgo.RxNetGo
import com.pingerx.rxnetgo.model.HttpParams


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
    fun getApi(): RxNetGo = getRxNetGo().getRetrofitService(getBaseUrl())


    /**
     * 对url获取某些参数进行签名
     */
    fun sign(params: HttpParams): String = params.toString()
}