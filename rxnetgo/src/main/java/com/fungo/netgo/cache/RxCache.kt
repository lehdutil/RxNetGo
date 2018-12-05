package com.fungo.netgo.cache

import com.google.gson.reflect.TypeToken
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.IFlowableStrategy
import com.zchu.rxcache.stategy.IObservableStrategy
import io.reactivex.*
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 上午11:40
 *
 * RxCache的Kotlin调用，这里不需要内联处理
 * 使用到的Type最好由外部传入，不要使用使用TypeToken
 *
 */

fun <T> RxCache.load(key: String): Observable<CacheResult<T>> {
    return load<T>(key, object : TypeToken<T>() {}.type)
}

fun <T> RxCache.load2Flowable(key: String): Flowable<CacheResult<T>> {
    return load2Flowable(key, object : TypeToken<T>() {}.type, BackpressureStrategy.LATEST)
}

fun <T> RxCache.load2Flowable(key: String, backpressureStrategy: BackpressureStrategy): Flowable<CacheResult<T>> {
    return load2Flowable(key, object : TypeToken<T>() {}.type, backpressureStrategy)
}

fun <T> RxCache.transformObservable(key: String, strategy: IObservableStrategy): ObservableTransformer<T, CacheResult<T>> {
    return transformObservable(key, object : TypeToken<T>() {}.type, strategy)
}

fun <T> RxCache.transformFlowable(key: String, strategy: IFlowableStrategy): FlowableTransformer<T, CacheResult<T>> {
    return transformFlowable(key, object : TypeToken<T>() {}.type, strategy)
}


fun <T> Observable<T>.rxCache(key: String, strategy: IObservableStrategy): Observable<CacheResult<T>> {
    return this.rxCache(RxCache.getDefault(), key, strategy)
}

fun <T> Observable<T>.rxCache(rxCache: RxCache, key: String, strategy: IObservableStrategy): Observable<CacheResult<T>> {
    return this.compose<CacheResult<T>>(rxCache.transformObservable(key, object : TypeToken<T>() {}.type, strategy))
}

fun <T> Flowable<T>.rxCache(key: String, type: Type, strategy: IFlowableStrategy): Flowable<CacheResult<T>> {
    return this.rxCache(RxCache.getDefault(), key, type, strategy)
}

fun <T> Flowable<T>.rxCache(rxCache: RxCache, key: String, strategy: IFlowableStrategy): Flowable<CacheResult<T>> {
    return this.rxCache(rxCache, key, object : TypeToken<T>() {}.type, strategy)
}

fun <T> Flowable<T>.rxCache(rxCache: RxCache, key: String, type: Type, strategy: IFlowableStrategy): Flowable<CacheResult<T>> {
    return this.compose<CacheResult<T>>(rxCache.transformFlowable(key, type, strategy))
}

