package com.fungo.netgo.request.base

import android.text.TextUtils
import com.fungo.netgo.NetGo
import com.fungo.netgo.cache.CacheApiProvider
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.callback.CallBack
import com.fungo.netgo.model.HttpHeaders
import com.fungo.netgo.model.HttpParams
import com.fungo.netgo.request.RequestType
import com.fungo.netgo.subscribe.RxSubscriber
import com.fungo.netgo.utils.RxUtils
import io.reactivex.Flowable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * @author Pinger
 * @since 18-10-23 上午11:03
 *
 *
 * 请求基类，封装请求
 */
abstract class Request<T>(
        private val url: String,                       // retrofit请求执行者
        private val apiService: ApiService,            // 缓存提供者
        private val mCacheProvider: CacheApiProvider?, // 主动包装的请求
        private val mFlowable: Flowable<T>?) {

    var cacheMode: CacheMode? = null
        private set
    var cacheKey: String? = null
        private set
    var retryCount: Int = 0
    var cacheTime: Long = 0


    private val params = HttpParams() //添加的param
    private val headers = HttpHeaders()  //添加的header

    var callBack: CallBack<T>? = null
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
        val response: Response<ResponseBody>? = when (getMethod()) {
            RequestType.GET -> getSync()
            RequestType.POST -> postSync()
        }

        return callBack?.convertResponse(response?.body())
    }

    /**
     * 订阅请求
     * 异步请求
     */
    fun subscribe(callBack: CallBack<T>) {
        this.callBack = callBack

        val requestFlowable: Flowable<Response<ResponseBody>> = when (getMethod()) {
            RequestType.GET -> getAsync()
            RequestType.POST -> postAsync()
        }

        requestFlowable
                .flatMap { response ->
                    Flowable.just(callBack.convertResponse(response.body()))
                }
                .compose(RxUtils.getScheduler())
                .onErrorResumeNext(RxUtils.getErrorFunction())
                .subscribe(RxSubscriber(callBack))
    }


    /**
     * 封装底层的Get异步请求
     */
    private fun getAsync(): Flowable<Response<ResponseBody>> {
        return apiService.getAsync(url, headers.getHeaderParams(), params.getUrlParams())
    }


    /**
     * 封装底层的Get同步请求
     */
    @Throws(Exception::class)
    private fun getSync(): Response<ResponseBody>? {
        return apiService.getSync(url, headers.getHeaderParams(), params.getUrlParams()).execute().body()
    }


    /**
     * 封装底层的Post异步请求
     */
    private fun postAsync(): Flowable<Response<ResponseBody>> {
        return apiService.postAsync(url, headers.getHeaderParams(), params.getUrlParams(), generateRequestBody())
    }


    /**
     * 封装底层的Post同步请求
     */
    @Throws(Exception::class)
    private fun postSync(): Response<ResponseBody>? {
        return apiService.postSync(url, headers.getHeaderParams(), params.getUrlParams(), generateRequestBody()).execute().body()
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
