package com.fungo.netgo.cache;

/**
 * @author Pinger
 * @since 18-10-17 上午11:32
 * 网络的缓存模式
 */
public enum CacheMode {

    /**
     * 缓存存在直接使用缓存，如果缓存不存在请求网络
     */
    ONLY_CACHE,

    /**
     * 只请求网络，不使用缓存。
     */
    ONLY_REQUEST,

    /**
     * 先使用缓存，不管是否存在，仍然请求网络覆盖。
     */
    FIRST_CACHE_THEN_REQUEST,


    /**
     * 先请求网络，并且进行缓存。如果网络请求失败，则使用缓存，缓存不存在就error。
     */
    FIRST_REQUEST_THEN_CACHE,

}
