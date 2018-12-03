package com.fungo.netgo.request.base

import android.text.TextUtils
import com.fungo.netgo.NetGo
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.cache.rxCache
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.model.HttpHeaders
import com.fungo.netgo.model.HttpParams
import com.fungo.netgo.request.RequestType
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.subscribe.base.IConverter
import com.fungo.netgo.utils.HttpUtils
import com.fungo.netgo.utils.RxUtils
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.CacheStrategy
import com.zchu.rxcache.stategy.IFlowableStrategy
import io.reactivex.Flowable
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-10-23 上午11:03
 *
 * 请求基类，封装请求
 *
 * 数据转换使用Converter的方式，如果是异步请求，回调继承[BaseSubscriber]实现converter方法即可。
 * 如果没有回调，则需要使用[Request.converter]方法，传入转换器
 *
 */
abstract class Request<T>(
        private val url: String,
        private val apiService: ApiService?,             // retrofit请求执行者
        private val mFlowable: Flowable<T>?) {

    private val mParams = HttpParams() //添加的param
    private val mHeaders = HttpHeaders()  //添加的header

    private var mSubscriber: BaseSubscriber<T>? = null

    private var mCacheMode: CacheMode? = null
    private var mCacheKey: String? = null
    private var mCacheTime: Long = 0
    private var mConverter: IConverter<T>? = null


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
        mCacheMode = netGo.cacheMode
        mCacheTime = netGo.cacheTime
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
        return mParams
    }


    /**
     * 获取请求头对象
     */
    fun getHeaders(): HttpHeaders {
        return mHeaders
    }


    /**
     * 同步请求
     */
    @Throws(Exception::class)
    fun subscribe(): T? {
        return requestSync(getConverter())
    }

    /**
     * 订阅请求
     * 异步请求
     */
    fun subscribe(subscriber: BaseSubscriber<T>) {
        this.mSubscriber = subscriber
        requestAsync(subscriber)
                .compose(RxUtils.getScheduler())
                .onErrorResumeNext(RxUtils.getErrorFunction())
//                .rxCache(getCacheKey(), getCacheStrategy())
//                .map(CacheResult.MapFunc<T>())
                .subscribe(subscriber)
    }

    /**
     * 缓存策略
     */
    private fun getCacheStrategy(): IFlowableStrategy {
        return when (mCacheMode) {
            CacheMode.FIRST_CACHE_THEN_REQUEST -> CacheStrategy.firstCache()
            CacheMode.FIRST_REQUEST_THEN_CACHE -> CacheStrategy.firstCache()
            CacheMode.ONLY_CACHE -> CacheStrategy.firstCache()
            else -> CacheStrategy.firstCache()
        }
    }


    /**
     * 缓存的key
     */
    private fun getCacheKey(): String {
        return if (TextUtils.isEmpty(mCacheKey)) {
            return HttpUtils.appendUrlParams(url, mParams.getUrlParams())
        } else mCacheKey!!
    }

    /**
     * 数据转换器
     */
    private fun getConverter(): IConverter<T> {
        // converter 优先级高于 callback
        if (mConverter == null) mConverter = mSubscriber
        HttpUtils.checkNotNull(mConverter, "converter == null, do you forget to call Request#converter(Converter<T>) ?")
        return mConverter!!
    }


    /**
     * 封装底层的异步请求
     */
    private fun requestAsync(subscriber: BaseSubscriber<T>): Flowable<T> {
        return when (getMethod()) {
            RequestType.GET -> {
                if (apiService != null) {
                    apiService.getAsync(url, mHeaders.getHeaderParams(), mParams.getUrlParams()).flatMap { response ->
                        Flowable.just(subscriber.convertResponse(response.body()))
                    }
                } else {
                    mFlowable
                            ?: Flowable.error<T>(ApiException("Rxnetgo async request engine not be null. Please retry!"))
                }
            }
            RequestType.POST -> {
                if (apiService != null) {
                    apiService.postAsync(url, mHeaders.getHeaderParams(), mParams.getUrlParams(), generateRequestBody()).flatMap { response ->
                        Flowable.just(subscriber.convertResponse(response.body()))
                    }
                } else {
                    mFlowable
                            ?: Flowable.error<T>(ApiException("Rxnetgo async request engine not be null. Please retry!"))
                }
            }
        }
    }


    /**
     * 封装底层的同步请求
     */
    @Throws(Exception::class)
    private fun requestSync(converter: IConverter<T>): T? {
        return when (getMethod()) {
            RequestType.GET -> {
                if (apiService != null) {
                    val response = apiService.getSync(url, mHeaders.getHeaderParams(), mParams.getUrlParams()).execute().body()
                    converter.convertResponse(response?.body())
                    null
                } else {
                    if (mFlowable != null) {
                        throw ApiException("Rxnetgo sync request do not support external customization. ")
                    }
                    null
                }
            }
            RequestType.POST -> {
                return if (apiService != null) {
                    val response = apiService.postSync(url, mHeaders.getHeaderParams(), mParams.getUrlParams(), generateRequestBody()).execute().body()
                    converter.convertResponse(response?.body())
                } else {
                    if (mFlowable != null) {
                        throw ApiException("Rxnetgo sync request do not support external customization. ")
                    }
                    null
                }
            }
        }
    }


    //---------------------------------------------------------------------
    //------------------------ 链式调用的API --------------------------------
    //---------------------------------------------------------------------
    fun headers(headers: HttpHeaders): Request<T> {
        this.mHeaders.put(headers)
        return this
    }

    fun headers(key: String, value: String?): Request<T> {
        this.mHeaders.put(key, value)
        return this
    }

    fun removeHeader(key: String): Request<T> {
        this.mHeaders.remove(key)
        return this
    }

    fun removeAllHeaders(): Request<T> {
        this.mHeaders.clear()
        return this
    }

    fun params(params: HttpParams): Request<T> {
        this.mParams.put(params)
        return this
    }

    fun params(params: Map<String, String>): Request<T> {
        this.mParams.put(params)
        return this
    }


    fun params(key: String, value: String): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun params(key: String, value: Int): Request<T> {
        this.mParams.put(key, value)
        return this
    }


    fun params(key: String, value: Boolean): Request<T> {
        this.mParams.put(key, value)
        return this
    }


    fun params(key: String, value: Float): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun params(key: String, value: Long): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun removeParam(key: String): Request<T> {
        this.mParams.remove(key)
        return this
    }

    fun removeAllParams(): Request<T> {
        this.mParams.clear()
        return this
    }

    fun converter(converter: IConverter<T>): Request<T> {
        HttpUtils.checkNotNull(converter, "converter == null")
        this.mConverter = converter
        return this
    }

    fun cacheMode(cacheMode: CacheMode): Request<T> {
        this.mCacheMode = cacheMode
        return this
    }

    fun cacheKey(cacheKey: String): Request<T> {
        this.mCacheKey = cacheKey
        return this
    }

    fun cacheTime(cacheTime: Long): Request<T> {
        this.mCacheTime = cacheTime
        return this
    }
}