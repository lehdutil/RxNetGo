package com.fungo.netgo.request.base

import android.text.TextUtils
import com.fungo.netgo.NetGo
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.model.HttpHeaders
import com.fungo.netgo.model.HttpParams
import com.fungo.netgo.request.RequestType
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.RxUtils
import io.reactivex.Flowable
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-10-23 上午11:03
 *
 *
 * 请求基类，封装请求
 */
abstract class Request<T>(
        private val url: String,
        private val apiService: ApiService?,             // retrofit请求执行者
        private val mFlowable: Flowable<T>?) {

    var cacheMode: CacheMode? = null
        private set
    var cacheKey: String? = null
        private set
    var retryCount: Int = 0
    var cacheTime: Long = 0


    private val params = HttpParams() //添加的param
    private val headers = HttpHeaders()  //添加的header

    var subscriber: BaseSubscriber<T>? = null
        private set

    init {

        val netGo = NetGo.getInstance()

        //默认添加 Accept-Language
        val acceptLanguage = HttpHeaders.acceptLanguage
        if (!TextUtils.isEmpty(acceptLanguage))
            headers(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage)

        //默认添加 User-Agent
        val userAgent = HttpHeaders.userAgent
        if (!TextUtils.isEmpty(userAgent)) headers(HttpHeaders.HEAD_KEY_USER_AGENT, userAgent)

        //添加公共请求参数
        if (netGo.commonParams != null) params(netGo.commonParams)
        if (netGo.commonHeaders != null) headers(netGo.commonHeaders)

        //添加缓存模式
        retryCount = netGo.retryCount
        cacheMode = netGo.cacheMode
        cacheTime = netGo.cacheTime
    }


    /**
     * 子类提供请求方式
     */
    abstract fun getMethod(): RequestType

    /**
     * 根据不同的请求方式和参数，生成不同的RequestBody
     */
    abstract fun generateRequestBody(): RequestBody?


    /**
     * 获取请求路径URL
     */
    fun getUrl(): String {
        return url
    }


    /**
     * 获取请求参数对象
     */
    fun getParams(): HttpParams {
        return params
    }


    /**
     * 获取请求头对象
     */
    fun getHeaders(): HttpHeaders {
        return headers
    }


    /**
     * 同步请求
     */
    @Throws(Exception::class)
    fun subscribe(): T? {
        val data: T? = when (getMethod()) {
            RequestType.GET -> getSync()
            RequestType.POST -> postSync()
        }

        return data
    }

    /**
     * 订阅请求
     * 异步请求
     */
    fun subscribe(subscriber: BaseSubscriber<T>) {
        this.subscriber = subscriber

        val requestFlowable: Flowable<T> = when (getMethod()) {
            RequestType.GET -> getAsync()
            RequestType.POST -> postAsync()
        }

        requestFlowable
                .compose(RxUtils.getScheduler())
                .onErrorResumeNext(RxUtils.getErrorFunction())
                .subscribe(subscriber)
    }


    /**
     * 封装底层的Get异步请求
     */
    private fun getAsync(): Flowable<T> {
        return if (apiService != null) {
            apiService.getAsync(url, headers.getHeaderParams(), params.getUrlParams()).flatMap { response ->
                Flowable.just(subscriber?.convertResponse(response.body()))
            }
        } else {
            mFlowable
                    ?: Flowable.error<T>(ApiException("Rxnetgo async request engine not be null. Please retry!"))
        }
    }


    /**
     * 封装底层的Get同步请求
     */
    @Throws(Exception::class)
    private fun getSync(): T? {
        return if (apiService != null) {
            val response = apiService.getSync(url, headers.getHeaderParams(), params.getUrlParams()).execute().body()
            subscriber?.convertResponse(response?.body())
        } else {
            if (mFlowable != null) {
                throw ApiException("Rxnetgo sync request do not support external customization. ")
            }
            null
        }
    }


    /**
     * 封装底层的Post异步请求
     */
    private fun postAsync(): Flowable<T> {
        return if (apiService != null) {
            apiService.postAsync(url, headers.getHeaderParams(), params.getUrlParams(), generateRequestBody()).flatMap { response ->
                Flowable.just(subscriber?.convertResponse(response.body()))
            }
        } else {
            mFlowable
                    ?: Flowable.error<T>(ApiException("Rxnetgo async request engine not be null. Please retry!"))
        }
    }


    /**
     * 封装底层的Post同步请求
     */
    @Throws(Exception::class)
    private fun postSync(): T? {
        return if (apiService != null) {
            val response = apiService.postSync(url, headers.getHeaderParams(), params.getUrlParams(), generateRequestBody()).execute().body()
            subscriber?.convertResponse(response?.body())
        } else {
            if (mFlowable != null) {
                throw ApiException("Rxnetgo sync request do not support external customization. ")
            }
            null
        }

    }

    //---------------------------------------------------------------------
    //------------------------ 链式调用的API --------------------------------
    //---------------------------------------------------------------------
    fun headers(headers: HttpHeaders): Request<T> {
        this.headers.put(headers)
        return this
    }

    fun headers(key: String, value: String?): Request<T> {
        this.headers.put(key, value)
        return this
    }

    fun removeHeader(key: String): Request<T> {
        this.headers.remove(key)
        return this
    }

    fun removeAllHeaders(): Request<T> {
        this.headers.clear()
        return this
    }

    fun params(params: HttpParams): Request<T> {
        this.params.put(params)
        return this
    }

    fun params(params: Map<String, String>): Request<T> {
        this.params.put(params)
        return this
    }


    fun params(key: String, value: String): Request<T> {
        this.params.put(key, value)
        return this
    }

    fun params(key: String, value: Int): Request<T> {
        this.params.put(key, value)
        return this
    }


    fun params(key: String, value: Boolean): Request<T> {
        this.params.put(key, value)
        return this
    }


    fun params(key: String, value: Float): Request<T> {
        this.params.put(key, value)
        return this
    }

    fun params(key: String, value: Long): Request<T> {
        this.params.put(key, value)
        return this
    }

    fun removeParam(key: String): Request<T> {
        this.params.remove(key)
        return this
    }

    fun removeAllParams(): Request<T> {
        this.params.clear()
        return this
    }


    fun cacheMode(cacheMode: CacheMode): Request<T> {
        this.cacheMode = cacheMode
        return this
    }

    fun cacheKey(cacheKey: String): Request<T> {
        this.cacheKey = cacheKey
        return this
    }
}
