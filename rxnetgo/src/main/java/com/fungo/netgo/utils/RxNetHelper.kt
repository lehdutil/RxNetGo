package com.fungo.netgo.utils

import com.fungo.netgo.cache.rxCache
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.exception.NetErrorEngine
import com.fungo.netgo.request.RequestType
import com.fungo.netgo.request.base.Request
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.RxNetHelper.cache
import com.fungo.netgo.utils.RxNetHelper.error
import com.fungo.netgo.utils.RxNetHelper.scheduler
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.IFlowableStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * @author Pinger
 * @since 18-12-5 下午4:39
 *
 * 网络请求工具类，提供请求相关的Rx操作
 * 线程切换：[scheduler]
 * 异常处理：[error]
 * 缓存：[cache]
 */
object RxNetHelper {

    /**
     * 异步请求
     */
    fun <T> rxAysnc(request: Request<T>): Flowable<T> {
        return when (request.getMethod()) {
            RequestType.GET -> getAsync(request)
            RequestType.POST -> postAsync(request)
        }
    }

    /**
     * 同步请求
     */
    @Throws(Exception::class)
    fun <T> rxSync(request: Request<T>): T? {
        return when (request.getMethod()) {
            RequestType.GET -> getSync(request)
            RequestType.POST -> postSync(request)
        }
    }


    /**
     * Rx异步的Get请求
     */
    fun <T> getAsync(request: Request<T>): Flowable<T> {
        return if (request.apiService != null) {
            request.apiService
                    .getAsync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams())
                    .flatMap { response ->
                        val data = request.getConverter().convertResponse(response)
                        if (data != null) {
                            Flowable.just(data)
                        } else {
                            Flowable.error(ApiException(msg = "Rxnetgo converter data is null!"))
                        }
                    }
                    .cache(request.getCacheKey(), request.getSubscriber(), request.getCacheStrategy())
                    .compose()
        } else {
            request.flowable
                    ?: Flowable.error(ApiException(msg = "Rxnetgo async request engine not be null. Please retry!"))
        }
    }

    /**
     * Rx异步的Post请求
     */
    fun <T> postAsync(request: Request<T>): Flowable<T> {
        return if (request.apiService != null) {
            request.apiService
                    .postAsync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams(), request.generateRequestBody())
                    .flatMap { response ->
                        val data = request.getConverter().convertResponse(response)
                        if (data != null) {
                            Flowable.just(data)
                        } else {
                            Flowable.error(ApiException(msg = "Rxnetgo converter data is null!"))
                        }
                    }.cache(request.getCacheKey(), request.getSubscriber(), request.getCacheStrategy())
                    .compose()
        } else {
            request.flowable
                    ?: Flowable.error(ApiException(msg = "Rxnetgo async request engine not be null. Please retry!"))
        }
    }


    /**
     * get同步请求，不需要使用Rx
     * TODO 同步请求的缓存手动处理
     */
    @Throws(Exception::class)
    fun <T> getSync(request: Request<T>): T? {
        return if (request.apiService != null) {
            val response = request.apiService
                    .getSync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams())
                    .execute()
                    .body()
            request.getConverter().convertResponse(response)
        } else {
            if (request.flowable != null) {
                throw ApiException(msg = "Rxnetgo sync request do not support external customization. ")
            }
            null
        }
    }


    /**
     * post同步请求，不需要使用Rx
     */
    @Throws(Exception::class)
    fun <T> postSync(request: Request<T>): T? {
        return if (request.apiService != null) {
            val response = request.apiService
                    .postSync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams(), request.generateRequestBody())
                    .execute()
                    .body()
            request.getConverter().convertResponse(response)
        } else {
            if (request.flowable != null) {
                throw ApiException(msg = "Rxnetgo sync request do not support external customization. ")
            }
            null
        }
    }


    /**
     * 生成组合线程切换，异常处理的Flowable
     */
    private fun <T> Flowable<T>.compose(): Flowable<T> {
        return scheduler().error()
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