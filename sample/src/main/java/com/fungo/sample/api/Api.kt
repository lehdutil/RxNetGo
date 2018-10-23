package com.fungo.sample.api

import com.fungo.netgo.NetGo
import io.reactivex.Flowable

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

    private const val API_BASE_URL = "http://gank.io/api/"

    private fun getApi(): NetGo {
        return NetGo.getInstance().getApi(API_BASE_URL)
    }


    fun getGankString(): Flowable<String> {
        val url = API_BASE_URL + "data/Android/30/1"
        return getApi().getRequest(url, String::class.java)
    }

}