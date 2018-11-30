package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheEntity;

/**
 * @author Pinger
 * @since 18-10-25 上午9:54
 * <p>
 * 缓存策略分发，定义回调接口
 */
public interface CachePolicy<T> {

    /**
     * 同步请求获取数据
     *
     * @return 从缓存或本地获取的数据
     */
    T requestSync();

    /**
     * 异步请求获取数据
     */
    void requestAsync();


    /**
     * 生成缓存数据对象
     */
    CacheEntity<T> prepareCache();
}
