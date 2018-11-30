package com.fungo.netgo.cache;

/**
 * @author Pinger
 * @since 18-10-25 上午11:05
 * <p>
 * 封装缓存的Flowable
 */
public class CacheFlowable<T> {

    public boolean isFromCache;

    public T data;


    public CacheFlowable(boolean isFromCache, T data) {
        this.isFromCache = isFromCache;
        this.data = data;
    }
}
