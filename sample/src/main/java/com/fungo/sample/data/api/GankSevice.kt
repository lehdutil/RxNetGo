package com.fungo.sample.data.api

import com.fungo.sample.data.bean.GankBean
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * @author Pinger
 * @since 18-12-3 上午10:18
 */
interface GankSevice {

    @GET("data/Android/30/1")
    fun getGankData(): Flowable<GankBean>

}