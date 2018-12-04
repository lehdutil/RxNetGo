package com.fungo.netgo.cache

/**
 * @author Pinger
 * @since 18-10-17 上午11:32
 * 网络的缓存模式
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
    FIRST_REQUEST_THEN_CACHE

}
