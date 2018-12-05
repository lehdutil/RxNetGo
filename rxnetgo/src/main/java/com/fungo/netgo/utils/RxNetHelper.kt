package com.fungo.netgo.utils

import com.fungo.netgo.cache.rxCache
import com.fungo.netgo.convert.base.IConverter
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.exception.NetErrorEngine
import com.fungo.netgo.request.base.ApiService
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.IFlowableStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-12-5 下午4:39
 *
 * 网络请求工具类
 */
object RxNetHelper {


    /**
     * Rx异步的Get请求
     */
    fun <T> getAsync(apiService: ApiService?,
                     flowable: Flowable<T>?,
                     url: String,
                     params: Map<String, Any>,
                     headers: Map<String, Any>,
                     converter: IConverter<T>): Flowable<T> {

        return if (apiService != null) {
            apiService.getAsync(url, headers, params).compose().flatMap { response ->
                val data = converter.convertResponse(response.body())
                if (data != null) {
                    Flowable.just(data)
                } else {
                    error("Rxnetgo converter data is null!")
                }
            }
        } else {
            flowable ?: error("Rxnetgo async request engine not be null. Please retry!")
        }
    }


    /**
     * Rx异步的Post请求
     */
    fun <T> postAsync(apiService: ApiService?,
                      flowable: Flowable<T>?,
                      url: String,
                      params: Map<String, Any>,
                      headers: Map<String, Any>,
                      requestBody: RequestBody?,
                      converter: IConverter<T>): Flowable<T> {
        return if (apiService != null) {
            apiService
                    .postAsync(url, headers, params, requestBody)
                    .compose()
                    .flatMap { response ->
                        val data = converter.convertResponse(response.body())
                        if (data != null) {
                            Flowable.just(data)
                        } else {
                            error("Rxnetgo converter data is null!")
                        }
                    }
        } else {
            flowable ?: error("Rxnetgo async request engine not be null. Please retry!")
        }
    }


    /**
     * get同步请求，不需要使用Rx
     */
    @Throws(Exception::class)
    fun <T> getSync(apiService: ApiService?,
                    flowable: Flowable<T>?,
                    url: String,
                    params: Map<String, Any>,
                    headers: Map<String, Any>,
                    converter: IConverter<T>): T? {
        return if (apiService != null) {
            val response = apiService
                    .getSync(url, headers, params)
                    .execute()
                    .body()
            converter.convertResponse(response?.body())
        } else {
            if (flowable != null) {
                throw ApiException(msg = "Rxnetgo sync request do not support external customization. ")
            }
            null
        }
    }


    /**
     * post同步请求，不需要使用Rx
     */
    @Throws(Exception::class)
    fun <T> postSync(apiService: ApiService?,
                     flowable: Flowable<T>?,
                     url: String,
                     params: Map<String, Any>,
                     headers: Map<String, Any>,
                     requestBody: RequestBody?,
                     converter: IConverter<T>): T? {
        return if (apiService != null) {
            val response = apiService
                    .postSync(url, headers, params, requestBody)
                    .execute()
                    .body()
            converter.convertResponse(response?.body())
        } else {
            if (flowable != null) {
                throw ApiException(msg = "Rxnetgo sync request do not support external customization. ")
            }
            null
        }
    }


    /**
     * 生成组合线程切换，异常处理，缓存的Flowable
     */
    private fun <T> Flowable<T>.compose(): Flowable<T> {
        return scheduler().error().cache()
    }


    /**
     * 线程切换
     */
    private fun <T> Flowable<T>.scheduler(): Flowable<T> {
        return subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }


    /**
     * 异常处理
     */
    private fun <T> Flowable<T>.error(msg: String? = null): Flowable<T> {
        return onErrorResumeNext(Function {
            Flowable.error<T> {
                val throwable = if (msg == null) it
                else ApiException(it, msg = msg)
                NetErrorEngine.handleException(throwable)
            }
        })
    }


    /**
     * 缓存
     */
    private fun <T> Flowable<T>.cache(cacheKey: String, subscriber: BaseSubscriber<T>, strategy: IFlowableStrategy): Flowable<T> {
        return rxCache(cacheKey, subscriber.getType(), strategy)
                .map(CacheResult.MapFunc<T>())
    }


}