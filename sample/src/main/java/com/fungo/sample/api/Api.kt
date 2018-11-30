package com.fungo.sample.api

import com.fungo.netgo.NetGo
import com.fungo.netgo.callback.StringCallBack

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

    private const val API_BASE_URL = "http://gank.io/api/"

    private fun getApi(): NetGo {
        return NetGo.getInstance().getRetrofitApi(API_BASE_URL)
    }


    fun getGankString(callBack: StringCallBack) {
        val url = API_BASE_URL + "data/Android/30/1"
        getApi().get<String>(url).subscribe(callBack)
    }
}