package com.fungo.netgo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fungo.netgo.cache.CacheMode;
import com.fungo.netgo.cookie.CookieJarImpl;
import com.fungo.netgo.cookie.store.MemoryCookieStore;
import com.fungo.netgo.https.HttpsUtils;
import com.fungo.netgo.interceptor.LogInterceptor;
import com.fungo.netgo.model.HttpHeaders;
import com.fungo.netgo.model.HttpParams;
import com.fungo.netgo.request.GetRequest;
import com.fungo.netgo.request.PostRequest;
import com.fungo.netgo.request.base.ApiService;
import com.fungo.netgo.utils.HttpUtils;
import com.fungo.netgo.utils.NetLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Pinger
 * @since 18-08-16 上午10:52
 * <p>
 * <p>
 * 网络类库封装：RxJava2 + Retrofit2封装
 */
public class NetGo {

    private static final String TAG = NetGo.class.getSimpleName();

    public static final long DEFAULT_MILLISECONDS = 15000;      // 默认的超时时间,10秒
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）
    public static final long CACHE_NEVER_EXPIRE = -1;           // 缓存永不过期

    private Context mContext;               //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient mClient;           //okhttp请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    // 当有多个baseurl时，需要build多个Retrofit实例，这里缓存 起来
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    private Map<String, ApiService> mServiceMap = new HashMap<>();

    private ApiService mService;

    /**
     * 生成默认的配置，如果想更新配置，可以使用内部的Builder更新
     */
    private NetGo() {

        mDelivery = new Handler(Looper.getMainLooper());

        mRetryCount = 3;
        mCacheTime = CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.REQUEST_FAILED_READ_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 日志打印
        LogInterceptor loggingInterceptor = new LogInterceptor("NetGo");
        loggingInterceptor.setPrintLevel(LogInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        // 超时
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        // https
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        // cookie
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        mClient = builder.build();
    }

    public static NetGo getInstance() {
        return NetGoHolder.holder;
    }

    private static class NetGoHolder {
        private static final NetGo holder = new NetGo();
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public NetGo init(Application app) {
        mContext = app.getApplicationContext();
        return this;
    }

    /**
     * 是否开发模式
     */
    public NetGo debug(boolean debug) {
        NetLogger.debug(TAG, debug);
        return this;
    }

    /**
     * 获取全局上下文
     */
    public Context getContext() {
        HttpUtils.checkNotNull(mContext, "please call NetGo.getInstance().init() first in application!");
        return mContext;
    }

    public Handler getDelivery() {
        HttpUtils.checkNotNull(mDelivery, "please call NetGo.getInstance().init() first in application!");
        return mDelivery;
    }


    /**
     * 手动设置OkHttpClient
     */
    public NetGo setOkHttpClient(OkHttpClient client) {
        HttpUtils.checkNotNull(client, "client can not be null");
        this.mClient = client;
        return this;
    }

    /**
     * 获取OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(mClient, "please call NetGo.getInstance().init() first in application!");
        return mClient;
    }


    /**
     * 如果要访问网络，首先就要生成Service
     * 根据url生成对应的Service
     */
    public NetGo getApi(String baseUrl) {
        HttpUtils.checkNotNull(baseUrl, "url can not be null");

        if (mServiceMap.get(baseUrl) == null) {
            mService = getRetrofitService(baseUrl, ApiService.class);
            mServiceMap.put(baseUrl, mService);
        } else {
            mService = mServiceMap.get(baseUrl);
        }
        return this;
    }

    /**
     * 获取Retrofit的Service
     */
    public <S> S getRetrofitService(String baseUrl, Class<S> service) {
        return getRetrofit(baseUrl).create(service);
    }

    /**
     * 根据baseurl获取Retrofit的实例
     */
    public Retrofit getRetrofit(String baseUrl) {
        HttpUtils.checkNotNull(baseUrl, "baseUrl can not be null");

        if (mRetrofitMap.get(baseUrl) != null) {
            return mRetrofitMap.get(baseUrl);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        mRetrofitMap.put(baseUrl, retrofit);
        return retrofit;
    }

    /**
     * 获取Retrofit的集合
     */
    public Map<String, Retrofit> getRetrofitMap() {
        return mRetrofitMap;
    }


    /**
     * 获取全局的cookie实例
     */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) mClient.cookieJar();
    }

    /**
     * 超时重试次数
     */
    public NetGo setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * 超时重试次数
     */
    public int getRetryCount() {
        return mRetryCount;
    }

    /**
     * 全局的缓存模式
     */
    public NetGo setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * 获取全局的缓存模式
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /**
     * 全局的缓存过期时间
     */
    public NetGo setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * 获取全局的缓存过期时间
     */
    public long getCacheTime() {
        return mCacheTime;
    }

    /**
     * 获取全局公共请求参数
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * 添加全局公共请求参数
     */
    public NetGo addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     */
    public NetGo addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * 清除retrofit缓存
     */
    public void clearCache() {
        mRetrofitMap.clear();
        mServiceMap.clear();
    }


    //=======================请求方式=======================
    //=======================请求方式=======================
    //=======================请求方式=======================


    /**
     * get请求,传入的可以是全路径url，也可以是其中的path
     * 当传入全路径url时会使用整个url访问
     */
    public <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url, mService);
    }


    /**
     * post请求
     */
    public <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url, mService);
    }
}
