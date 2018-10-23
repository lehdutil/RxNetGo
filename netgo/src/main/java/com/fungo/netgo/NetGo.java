package com.fungo.netgo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fungo.netgo.cache.CacheMode;
import com.fungo.netgo.cookie.CookieJarImpl;
import com.fungo.netgo.cookie.store.MemoryCookieStore;
import com.fungo.netgo.error.ApiException;
import com.fungo.netgo.error.NetError;
import com.fungo.netgo.https.HttpsUtils;
import com.fungo.netgo.interceptor.LogInterceptor;
import com.fungo.netgo.model.HttpHeaders;
import com.fungo.netgo.model.HttpParams;
import com.fungo.netgo.request.BaseBodyRequest;
import com.fungo.netgo.request.FungoApi;
import com.fungo.netgo.request.RequestType;
import com.fungo.netgo.utils.GsonUtils;
import com.fungo.netgo.utils.HttpUtils;
import com.fungo.netgo.utils.NetLogger;
import com.google.gson.JsonElement;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
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
    private Map<String, FungoApi> mFungoMap = new HashMap<>();
    private FungoApi mFungoApi;


    /**
     * 生成默认的配置
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
        private static NetGo holder = new NetGo();
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
     * 根据url生成对应的Service
     */
    public NetGo getApi(String url) {
        HttpUtils.checkNotNull(url, "url can not be null");

        if (mFungoMap.get(url) == null) {
            mFungoApi = getRetrofitService(url, FungoApi.class);
            mFungoMap.put(url, mFungoApi);
        } else {
            mFungoApi = mFungoMap.get(url);
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
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 清除retrofit缓存
     */
    public void clearCache() {
        mRetrofitMap.clear();
        mFungoMap.clear();
    }


    //=======================请求方式=======================
    //=======================请求方式=======================
    //=======================请求方式=======================

    // TODO 将请求参数抽取出去，通过Builder的方式设置各个参数

    /**
     * GET请求，不带任何参数
     */
    public <T> Flowable<T> getRequest(String url, Class<T> clazz) {
        return request(RequestType.GET, url, null, clazz);
    }

    public <T> Flowable<T> getRequest(String url) {
        return request(RequestType.GET, url, null, null);
    }

    /**
     * GET请求，带有参数
     */
    public <T> Flowable<T> getRequest(String url, Map<String, Object> params, Class<T> clazz) {
        return request(RequestType.GET, url, params, clazz);
    }


    /**
     * POST请求，请求体不带参数
     */
    public <T> Flowable<T> postRequest(String url, Class<T> clazz) {
        return request(RequestType.POST, url, null, clazz);
    }

    /**
     * POST请求，带有参数
     */
    public <T> Flowable<T> postRequest(String url, Map<String, Object> params, Class<T> clazz) {
        return request(RequestType.POST, url, params, clazz);
    }


    /**
     * 网络请求核心业务
     * <p>
     * 由于解析数据的时候发现解析方法上泛型会发生泛型擦除，确定不了T的类型，所有这里将数据类型由外部传入进入
     */
    private <T> Flowable<T> request(final RequestType type, final String url, final Map<String, Object> params, final Class<T> clazz) {
        if (HttpUtils.isNetConnected(mContext)) {

            return Flowable
                    .just(url)
                    .flatMap(new Function<String, Publisher<JsonElement>>() {
                        @Override
                        public Publisher<JsonElement> apply(String s) {

                            // 重组请求链接和参数
                            if (type == RequestType.GET) {
                                String fullUrl = HttpUtils.appendUrlParams(url, params);
                                if (fullUrl.contains("http") || fullUrl.contains("https")) {
                                    return mFungoApi.getRequestWithFullUrl(fullUrl);
                                } else {
                                    return mFungoApi.getRequest(fullUrl);
                                }

                            } else {
                                BaseBodyRequest bodyParams = HttpUtils.getPostBody(params);
                                if (url.contains("http") || url.contains("https")) {
                                    return mFungoApi.postRequestWithFullUrl(url, bodyParams);
                                } else {
                                    return mFungoApi.postRequest(url, bodyParams);
                                }
                            }
                        }
                    })
                    .flatMap(new Function<JsonElement, Publisher<T>>() {
                        @Override
                        public Publisher<T> apply(JsonElement jsonElement) {
                            if (jsonElement == null) {
                                return Flowable.error(new ApiException(NetError.MSG_ERROR_DATA, NetError.ERROR_DATA));
                            } else {
                                try {
                                    // 对class的类型进行区分，不同的class做不同的处理
                                    T t;
                                    if (clazz == null || clazz == String.class) {
                                        t = (T) jsonElement.toString();
                                    } else {
                                        // 其他类型的数据类型可以直接转换
                                        t = GsonUtils.INSTANCE.fromJson(jsonElement, clazz);
                                    }

                                    if (t == null) {
                                        return Flowable.error(new ApiException(NetError.MSG_ERROR_DATA, NetError.ERROR_DATA));
                                    } else {
                                        return Flowable.just(t);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return requestError(NetError.MSG_PARSE_ERROR, NetError.PARSE_ERROR);
                                }
                            }
                        }
                    })

                    // 请求过程中和请求相应后的异常处理
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                        @Override
                        public Publisher<? extends T> apply(Throwable throwable) {
                            return Flowable.error(NetError.handleException(throwable));
                        }

                    })
                    // 网络请求线程自动切换
                    .compose(this.<T>getScheduler());
        } else {
            // 没有网络时，直接进入异常状态
            return Flowable.create(new FlowableOnSubscribe<T>() {
                @Override
                public void subscribe(FlowableEmitter<T> e) {
                    if (!e.isCancelled()) {
                        e.onError(new ApiException(NetError.MSG_NET_BREAK, NetError.NET_BREAK));
                        e.onComplete();
                    }
                }
            }, BackpressureStrategy.BUFFER);
        }
    }


    /**
     * 内部的错误处理封装
     */
    private <T> Flowable<T> requestError(String message, int code) {
        return Flowable.error(new ApiException(message, code));
    }

    /**
     * 线程切换
     */
    private <T> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
