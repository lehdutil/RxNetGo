package com.fungo.netgo.cache

import io.reactivex.Flowable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictProvider
import io.rx_cache2.LifeCache
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

/**
 * @author Pinger
 * @since 18-11-30 上午10:23
 *
 * 缓存提供者，结合Retrofit使用，方法名必须和[com.fungo.netgo.request.base.ApiService]中的一样
 * 缓存时间默认为1天
 *
 */
interface CacheApiProvider {

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    fun postAsync(flowable: Flowable<Response<ResponseBody>>, dynamicKey: DynamicKey, evictProvider: EvictProvider): Flowable<Response<ResponseBody>>

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    fun postSync(call: Call<Response<ResponseBody>>, dynamicKey: DynamicKey, evictProvider: EvictProvider): Call<Response<ResponseBody>>

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    fun getAsync(flowable: Flowable<Response<ResponseBody>>, dynamicKey: DynamicKey, evictProvider: EvictProvider): Flowable<Response<ResponseBody>>

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    fun getSync(call: Call<Response<ResponseBody>>, dynamicKey: DynamicKey, evictProvider: EvictProvider): Call<Response<ResponseBody>>
}