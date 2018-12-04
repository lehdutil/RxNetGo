package com.fungo.netgo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.fungo.netgo.cache.CacheMode
import com.fungo.netgo.cookie.CookieJarImpl
import com.fungo.netgo.cookie.store.PersistentCookieStore
import com.fungo.netgo.exception.NetErrorEngine
import com.fungo.netgo.https.HttpsUtils
import com.fungo.netgo.interceptor.LogInterceptor
import com.fungo.netgo.model.HttpHeaders
import com.fungo.netgo.model.HttpParams
import com.fungo.netgo.request.GetRequest
import com.fungo.netgo.request.PostRequest
import com.fungo.netgo.request.base.ApiService
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.HttpUtils
import com.fungo.netgo.utils.NetLogger
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.diskconverter.GsonDiskConverter
import io.reactivex.Flowable
import io.reactivex.functions.Function
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * @author Pinger
 * @since 18-08-16 上午10:52
 *
 * 网络类库封装：RxJava2 + Retrofit2封装
 *
 * 网络默认配置：
 *  １．各类超时时间统一默认：10秒
 *  ２．缓存模式默认：先请求后缓存，网络异常时读取缓存
 *  ３．默认支持Https
 *  ４．Cookie默认长久保存在SP中
 *
 *
 *
 *
 */
class RxNetGo {

    private var mContext: Context? = null               // 全局上下文
    private var mClient: OkHttpClient? = null           // okhttp请求的客户端

    private var commonParams: HttpParams = HttpParams()       // 全局公共请求参数
    private var commonHeaders: HttpHeaders = HttpHeaders()    // 全局公共请求头

    private var mRetryCount: Int = 0                // 全局超时重试次数
    private var mCacheMode: CacheMode? = null       // 全局缓存模式
    private var mCacheTime: Long = 0                // 全局缓存过期时间,默认永不过期
    private var mCacheVersion: Int = 0              // 全局缓存版本，如果版本升级，则会清空之前的所有缓存

    // 当有多个baseurl时，需要build多个Retrofit实例，这里缓存 起来
    private val mRetrofitMap = HashMap<String, Retrofit>()
    private val mServiceMap = HashMap<String, ApiService>()

    private var mService: ApiService? = null


    companion object {

        private val TAG = RxNetGo::class.java.simpleName

        const val DEFAULT_MILLISECONDS: Long = 10000      // 默认的超时时间,10秒
        const val CACHE_NEVER_EXPIRE: Long = -1           // 缓存永不过期

        val instance: RxNetGo
            get() = NetGoHolder.holder
    }

    @SuppressLint("StaticFieldLeak")
    private object NetGoHolder {
        val holder = RxNetGo()
    }


    /**
     * 生成默认的配置，如果想更新配置，可以使用内部的Builder更新
     */
    private fun initNetGo() {
        mRetryCount = 3
        mCacheTime = CACHE_NEVER_EXPIRE
        // 默认不使用缓存
        mCacheMode = CacheMode.FIRST_REQUEST_THEN_CACHE
        // 缓存版本如果不修改，则缓存永远进行覆盖
        mCacheVersion = 1

        val builder = OkHttpClient.Builder()

        // 日志打印
        val loggingInterceptor = LogInterceptor(TAG)
        loggingInterceptor.setPrintLevel(LogInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        builder.addInterceptor(loggingInterceptor)

        // 超时
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)

        // https
        val sslParams = HttpsUtils.sslSocketFactory
        builder.sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)

        // cookie
        builder.cookieJar(CookieJarImpl(PersistentCookieStore(getContext())))

        mClient = builder.build()

        // RxCache
        val rxCache = RxCache.Builder()
                .setDebug(NetLogger.isDebug)
                .appVersion(mCacheVersion)  //当版本号改变,缓存路径下存储的所有数据都会被清除掉
                .diskDir(HttpUtils.getCacheFile(getContext()))
                .diskConverter(GsonDiskConverter())  //支持Serializable、Json(GsonDiskConverter)
                .memoryMax(2 * 1024 * 1024)       // 2M内存缓存
                .diskMax((100 * 1024 * 1024).toLong())       // 100M硬盘缓存
                .build()
        RxCache.initializeDefault(rxCache)
    }


    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    fun init(app: Application): RxNetGo {
        mContext = app.applicationContext
        initNetGo()
        return this
    }


    /**
     * 是否开发模式
     */
    fun debug(debug: Boolean): RxNetGo {
        NetLogger.debug(TAG, debug)
        return this
    }

    /**
     * 获取全局公共请求参数
     */
    fun getCommonParams(): HttpParams {
        return commonParams
    }

    /**
     * 获取全局公共请求头
     */
    fun getCommonHeaders(): HttpHeaders {
        return commonHeaders
    }


    /**
     * 获取全局上下文
     */
    fun getContext(): Context {
        HttpUtils.checkNotNull(mContext, "please call RxNetGo.getInstance().init() first in application!")
        return mContext!!
    }


    /**
     * 获取OkHttpClient
     */
    fun getOkHttpClient(): OkHttpClient {
        HttpUtils.checkNotNull<Any>(mClient, "please call RxNetGo.getInstance().init() first in application!")
        return mClient!!
    }

    /**
     * 获取Retrofit的集合
     */
    fun getRetrofits(): Map<String, Retrofit> {
        return mRetrofitMap
    }


    /**
     * 获取全局的cookie实例
     */
    fun getCookieJar(): CookieJarImpl {
        return getOkHttpClient().cookieJar() as CookieJarImpl
    }

    /**
     * 手动设置OkHttpClient
     */
    fun setOkHttpClient(client: OkHttpClient): RxNetGo {
        HttpUtils.checkNotNull<Any>(client, "client can not be null")
        this.mClient = client
        return this
    }

    /**
     * 如果使用Retrofit，首先就要生成Service
     * 根据url生成对应的Service,Service可能会有多个，所有这里用Map集合存起来，避免重复创建
     * 这里使用默认的[ApiService]，可以直接调用get和post请求网络
     */
    fun getRetrofitService(baseUrl: String): RxNetGo {
        HttpUtils.checkNotNull<Any>(baseUrl, "url can not be null")

        if (mServiceMap[baseUrl] == null) {
            mService = getRetrofitService(baseUrl, ApiService::class.java)
            mServiceMap[baseUrl] = mService!!
        } else {
            mService = mServiceMap[baseUrl]
        }

        return this
    }

    /**
     * 获取用户自定义的Retrofit的Service，使用自定义的Service时，需要手动传入Service生成的Flowable
     * [get(flowable)]
     */
    fun <S> getRetrofitService(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl).create(service)
    }

    /**
     * 根据baseurl获取Retrofit的实例
     */
    private fun getRetrofit(baseUrl: String): Retrofit {
        HttpUtils.checkNotNull<Any>(baseUrl, "baseUrl can not be null")

        if (getRetrofits()[baseUrl] != null) {
            return getRetrofits()[baseUrl]!!
        }

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        val retrofit = builder.build()
        mRetrofitMap[baseUrl] = retrofit
        return retrofit
    }

    /**
     * 超时重试次数
     */
    fun setRetryCount(retryCount: Int): RxNetGo {
        if (retryCount < 0) throw IllegalArgumentException("retryCount must > 0")
        mRetryCount = retryCount
        return this
    }

    /**
     * 超时重试次数
     */
    fun getRetryCount(): Int {
        return mRetryCount
    }

    /**
     * 全局的缓存模式
     */
    fun setCacheMode(cacheMode: CacheMode): RxNetGo {
        mCacheMode = cacheMode
        return this
    }

    /**
     * 获取全局的缓存模式
     */
    fun getCacheMode(): CacheMode? {
        return mCacheMode
    }

    /**
     * 全局的缓存过期时间
     */
    fun setCacheTime(cacheTime: Long): RxNetGo {
        if (cacheTime <= -1) mCacheTime = CACHE_NEVER_EXPIRE
        mCacheTime = cacheTime
        return this
    }

    fun setCacheVersion(cacheVersion: Int): RxNetGo {
        this.mCacheVersion = cacheVersion
        return this
    }


    /**
     * 获取全局的缓存过期时间
     */
    fun getCacheTime(): Long {
        return mCacheTime
    }

    /**
     * 添加全局公共请求参数
     */
    fun addCommonParams(commonParams: HttpParams): RxNetGo {
        this.commonParams.put(commonParams)
        return this
    }

    /**
     * 添加全局公共请求参数
     */
    fun addCommonHeaders(commonHeaders: HttpHeaders): RxNetGo {
        this.commonHeaders.put(commonHeaders)
        return this
    }

    /**
     * 清除网络数据缓存
     */
    fun clearCache() {
        RxCache.getDefault().clear().subscribe()
    }


    //=======================Request API=======================
    //=======================Request API=======================
    //=======================Request API=======================
    /**
     * get请求,传入的可以是全路径url，也可以是其中的path
     * 当传入全路径url时会使用整个url访问
     */
    operator fun <T> get(url: String): GetRequest<T> {
        return GetRequest(url, mService, null)
    }


    /**
     * get请求，由外部传入Retrofit组织好的Flowable进来
     */
    operator fun <T> get(flowable: Flowable<T>): GetRequest<T> {
        return GetRequest("", mService, flowable)
    }


    /**
     * post请求，需要先初始化对应的Service
     */
    fun <T> post(url: String): PostRequest<T> {
        return PostRequest(url, mService, null)
    }

    /**
     * post请求，由用户自定义Service
     */
    fun <T> post(flowable: Flowable<T>): PostRequest<T> {
        return PostRequest("", mService, flowable)
    }


    //=======================Cache API=======================
    //=======================Cache API=======================
    //=======================Cache API=======================

    /**
     * 手动读取缓存
     */
    fun <T> loadCache(key: String, subscriber: BaseSubscriber<T>) {
        RxCache.getDefault()
                .load2Flowable<T>(key, subscriber.getType())
                .map(CacheResult.MapFunc())
                .onErrorResumeNext(Function { Flowable.error(NetErrorEngine.handleException(it)) })
                .subscribe(subscriber)
    }


    /**
     * 手动保存缓存到内存和磁盘
     */
    fun <T> saveCache(key: String, data: T) {
        RxCache.getDefault().save(key, data).subscribe()
    }

}
