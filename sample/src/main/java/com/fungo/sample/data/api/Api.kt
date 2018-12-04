package com.fungo.sample.data.api

import com.fungo.netgo.NetGo
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.bean.GankBean

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

    private const val API_BASE_URL = "http://gank.io/api/"

    fun getNetGo(): NetGo {
        return NetGo.getInstance()
    }


    fun getApiService(): NetGo {
        return getNetGo().getRetrofitApi(API_BASE_URL)
    }


    fun getGankService(): GankSevice {
        return getNetGo().getRetrofitService(API_BASE_URL, GankSevice::class.java)
    }


    fun getGankData(subscriber: JsonSubscriber<GankBean>) {
//        getNetGo().get<GankBean>(getGankService().getGankData()).subscribe(subscriber)
        getApiService()
                .get<GankBean>("data/Android/30/1")
                .cacheKey("gank_data")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .subscribe(subscriber)

    }
}