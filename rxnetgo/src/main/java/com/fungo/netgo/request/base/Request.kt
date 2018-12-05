package com.fungo.netgo.request.base

import android.text.TextUtils
import com.fungo.netgo.RxNetGo
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.convert.base.IConverter
import com.fungo.netgo.model.HttpHeaders
import com.fungo.netgo.model.HttpParams
import com.fungo.netgo.request.RequestType
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.HttpUtils
import com.fungo.netgo.utils.RxNetHelper
import com.zchu.rxcache.stategy.CacheStrategy
import com.zchu.rxcache.stategy.IFlowableStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-10-23 上午11:03
 *
 * 请求基类，封装请求，包括配置请求参数，请求头，缓存，和订阅请求等等。
 *
 * 数据转换使用Converter的方式，如果是异步请求，回调继承[BaseSubscriber]实现converter方法即可。
 * 如果没有回调，则需要使用[Request.converter]方法，传入转换器
 *
 */
abstract class Request<T>(
        // 请求的path或者全路径
        private val url: String,
        // retrofit的service，用户自定义service时为null
        private val apiService: ApiService?,
        // service中定义的观察者，如果使用默认的service则为null
        private val mFlowable: Flowable<T>?) {

    // 请求参数
    private val mParams = HttpParams()
    //　请求头
    private val mHeaders = HttpHeaders()

    // 订阅者
    private var mSubscriber: BaseSubscriber<T>? = null

    // 缓存
    private var mCacheKey: String? = null
    private var mCacheMode: CacheMode? = null
    private var mCacheTime: Long = 0

    // 转换器，等级比BaseSubscriber高
    private var mConverter: IConverter<T>? = null

    private val mRxNetGo = RxNetGo.getInstance()

    init {
        // 默认添加 Accept-Language
        val acceptLanguage = HttpHeaders.acceptLanguage
        if (!TextUtils.isEmpty(acceptLanguage))
            headers(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage)

        // 默认添加 User-Agent
        val userAgent = HttpHeaders.userAgent
        if (!TextUtils.isEmpty(userAgent)) headers(HttpHeaders.HEAD_KEY_USER_AGENT, userAgent)

        // 添加公共请求参数
        if (mRxNetGo.getCommonParams().getUrlParams().isNotEmpty())
            params(mRxNetGo.getCommonParams())
        if (mRxNetGo.getCommonHeaders().getHeaderParams().isNotEmpty())
            headers(mRxNetGo.getCommonHeaders())

        // 添加缓存模式
        mCacheMode = mRxNetGo.getCacheMode()
        mCacheTime = mRxNetGo.getCacheTime()
    }


    /**
     * 子类提供请求方式，调用[RxNetGo.get]或者[RxNetGo.post]
     */
    protected abstract fun getMethod(): RequestType

    /**
     * 根据不同的请求方式和参数，生成不同的RequestBody
     * 如果发起的请求中要自定义请求体，可以继承[BodyRequest]实现
     */
    protected abstract fun generateRequestBody(): RequestBody?


    /**
     * 获取请求路径URL，可以是path，也可以全路径
     */
    protected fun getUrl(): String {
        return url
    }


    /**
     * 获取请求参数对象
     */
    protected fun getParams(): HttpParams {
        return mParams
    }


    /**
     * 获取请求头对象
     * 获取的请求头包括了公共请求头
     * 添加公共请求头请使用[RxNetGo.addCommonHeaders]
     */
    protected fun getHeaders(): HttpHeaders {
        return mHeaders
    }

    /**
     * 缓存使用的是[com.zchu.rxcache.RxCache]，地址[https://github.com/z-chu/RxCache]
     * 暂时提供四种缓存策略[CacheMode]
     */
    private fun getCacheStrategy(): IFlowableStrategy {
        return when (mCacheMode) {
            CacheMode.FIRST_CACHE_THEN_REQUEST -> CacheStrategy.firstCache()
            CacheMode.FIRST_REQUEST_THEN_CACHE -> CacheStrategy.firstRemote()
            CacheMode.ONLY_CACHE -> CacheStrategy.onlyCache()
            else -> CacheStrategy.onlyRemote()
        }
    }


    /**
     * 请求唯一的key
     */
    private fun getCacheKey(): String {
        return if (TextUtils.isEmpty(mCacheKey)) {
            return HttpUtils.appendUrlParams(url, mParams.getUrlParams())
        } else mCacheKey!!
    }

    /**
     * 数据转换器，异步方法的回调对象默认也是转换器，或者通过[converter]方法传入
     *
     * 异步请求可以通过[BaseSubscriber.convertResponse]方法解析数据
     * 同步请求一定要调用[converter]传入解析器，否则解析不了数据
     */
    private fun getConverter(): IConverter<T> {
        // converter 优先级高于 callback
        if (mConverter == null) mConverter = mSubscriber
        HttpUtils.checkNotNull(mConverter, "converter == null, do you forget to call Request#converter(Converter<T>) ?")
        return mConverter!!
    }


    //---------------------------------------------------------------------
    //------------------------ 订阅请求的API --------------------------------
    //---------------------------------------------------------------------
    /**
     * 同步请求，会阻塞线程
     *
     * 注意：
     *  １．必须在子线程中调用，如果要更新UI，需要手动切换线程。
     *  ２．必须手动捕获异常。
     */
    @Throws(Exception::class)
    fun subscribe(): T? {
        return when (getMethod()) {
            RequestType.GET -> RxNetHelper.getSync(apiService, mFlowable, url,
                    mParams.getUrlParams(), mHeaders.getHeaderParams(), getConverter())

            RequestType.POST -> RxNetHelper.postSync(apiService, mFlowable, url,
                    mParams.getUrlParams(), mHeaders.getHeaderParams(),
                    generateRequestBody(), getConverter())
        }
    }

    /**
     * 订阅异步请求，在回调中处理结果，订阅者可以自定义继承[BaseSubscriber]就可以手动去处理数据
     * 返回回调方法[BaseSubscriber.onNext]中，数据T一定不为空，如果解析出数据为null则会回调[BaseSubscriber.onError]方法
     *
     * 本默认会将发起的订阅加入管理器中，如果要取消订阅可以调用[RxNetGo.dispose]方法
     * 加入管理器后，回调的[BaseSubscriber.onError]和[BaseSubscriber.onComplete]方法执行都会调用[BaseSubscriber.dispose]方法取消订阅
     * 如果需要自己处理订阅生命周期，可以调用[subscribeWith]方法
     */
    fun subscribe(subscriber: BaseSubscriber<T>) {
        this.mSubscriber = subscriber

        val flowable = when (getMethod()) {
            RequestType.GET -> RxNetHelper.getAsync(apiService, mFlowable, url,
                    mParams.getUrlParams(), mHeaders.getHeaderParams(), subscriber)

            RequestType.POST ->
                RxNetHelper.postAsync(apiService, mFlowable, url,
                        mParams.getUrlParams(), mHeaders.getHeaderParams(),
                        generateRequestBody(), subscriber)
        }

        val disposable = flowable.subscribeWith(subscriber)

        // 将请求添加到管理器中
        mRxNetGo.addSubscription(disposable)
    }


    /**
     * 订阅异步请求，返回[Disposable]对象，需要自己手动处理请求生命周期
     * 如果自己处理订阅，回调中的方法都不会去取消订阅，如果自己不去处理，有可能会发生内存泄露
     *
     * 推荐使用[subscribe]方法，管理生命周期更加灵活
     */
    fun subscribeWith(subscriber: BaseSubscriber<T>): Disposable {
        this.mSubscriber = subscriber
        return when (getMethod()) {
            RequestType.GET -> RxNetHelper.getAsync(apiService, mFlowable, url,
                    mParams.getUrlParams(), mHeaders.getHeaderParams(), subscriber)

            RequestType.POST ->
                RxNetHelper.postAsync(apiService, mFlowable, url,
                        mParams.getUrlParams(), mHeaders.getHeaderParams(),
                        generateRequestBody(), subscriber)
        }.subscribeWith(subscriber)
    }

    //---------------------------------------------------------------------
    //------------------------ 请求配置的API --------------------------------
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