package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 *
 * 有缓存直接返回成功
 * 没有缓存则继续请求网络，请求成功则返回成功，请求失败则返回失败
 *
 */
public class NoneCacheRequestPolicy<T> extends BaseCachePolicy<T> {

    public NoneCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        Flowable.concat(prepareCacheFlowable(), prepareAsyncRequestFlowable())
                .firstElement()
                .toFlowable()
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }


    @Override
    protected Flowable<T> prepareCacheFlowable() {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) {
                if (!emitter.isCancelled()) {
                    CacheEntity<T> cacheEntity = prepareCache();
                    // 有缓存则直接返回缓存
                    if (cacheEntity != null) {
                        emitter.onNext(cacheEntity.getData());
                    } else {
                        // 没有缓存时才去请求网络
                    }
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
