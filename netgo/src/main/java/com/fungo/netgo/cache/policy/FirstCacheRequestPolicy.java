package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 * <p>
 * 有缓存先读取缓存返回
 * 然后再继续请求网络
 * 没有缓存则继续请求网络
 */
public class FirstCacheRequestPolicy<T> extends BaseCachePolicy<T> {

    public FirstCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        Flowable.concat(prepareCacheFlowable(), prepareAsyncRequestFlowable())
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }


    @Override
    Flowable<T> prepareAsyncRequestFlowable() {
        return super.prepareAsyncRequestFlowable().onErrorResumeNext(new Publisher<T>() {
            @Override
            public void subscribe(Subscriber<? super T> subscriber) {
                // 如果有缓存，但是又网络请求失败了，就不去调用onError覆盖缓存，直接结束
                subscriber.onComplete();
            }
        });
    }
}
