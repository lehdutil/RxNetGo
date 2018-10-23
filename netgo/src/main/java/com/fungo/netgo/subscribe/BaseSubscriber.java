package com.fungo.netgo.subscribe;

import com.fungo.netgo.error.ApiException;
import com.fungo.netgo.error.NetError;

/**
 * @author Pinger
 * @since 18-10-19 上午11:08
 * <p>
 * 订阅者基类，让子类去实现各种订阅者，可以是Json，String或者不需要返回
 */
public abstract class BaseSubscriber<T> extends ResourceSubscriber<T> {


    /**
     * 分发一下异常
     * TODO 判断缓存策略
     */
    @Override
    final public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, NetError.UNKNOWN));
        }
    }

    /**
     * 请求异常，给子类去实现
     */
    protected void onError(ApiException e) {
    }


    @Override
    public void onComplete() {

    }

}
