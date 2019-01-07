package com.fungo.netgo.cache

/**
 * @author Pinger
 * @since 18-10-17 上午11:32
 *
 * 网络请求的缓存模式
 *
 * 缓存使用的是第三方框架RxCache，地址[https://github.com/z-chu/RxCache]
 * 这里暂时只提供四种常见的缓存策略，更多的策略请查看[com.zchu.rxcache.stategy.CacheStrategy]
 *
 */
enum class CacheMode {

    /**
     * 只使用缓存，不会请求网络。如果没有缓存，则无数据返回。
     */
    ONLY_CACHE,

    /**
     * 只请求网络，不使用缓存。请求失败了也不会使用缓存。
     */
    ONLY_REQUEST,

    /**
     * 先使用缓存，不管是否存在，仍然请求网络覆盖缓存，页面只会更新一次。
     */
    FIRST_CACHE_THEN_REQUEST,


    /**
     * 正常请求网络数据，并且进行缓存。如果网络请求失败，则使用缓存，缓存不存在就error。
     */
    FIRST_REQUEST_THEN_CACHE,


    /**
     * 先读取缓存，然后请求网络覆盖缓存，如果有缓存会覆盖两次
     */
    FIRST_CACHE_AND_REQUEST

}
