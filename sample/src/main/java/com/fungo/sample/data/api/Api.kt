package com.fungo.sample.data.api

import com.fungo.netgo.NetGo
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.netgo.subscribe.StringSubscriber
import com.fungo.sample.data.bean.GankBean

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

    private const val API_BASE_URL = "http://gank.io/api/"

    private fun getApi(): NetGo {
        return NetGo.getInstance()
    }


    private fun getApiService():NetGo{
        return NetGo.getInstance().getRetrofitApi(API_BASE_URL)
    }


    private fun getService(): GankSevice {
        return NetGo.getInstance().getRetrofitService(API_BASE_URL, GankSevice::class.java)
    }


    fun getGankString(subscriber: JsonSubscriber<GankBean>) {
//        getApi().get<GankBean>(getService().getGankData()).subscribe(subscriber)
        getApiService().get<GankBean>("data/Android/30/1").subscribe(subscriber)

    }
}