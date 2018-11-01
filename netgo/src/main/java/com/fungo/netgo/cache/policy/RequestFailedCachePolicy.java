package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.exception.ApiException;
import com.fungo.netgo.exception.NetError;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * @author Pinger
 * @since 18-10-25 上午10:09
 * <p>
 * 先发起请求。
 * 如果请求成功，则返回成功
 * 如果请求失败，则读取缓存
 * 如果有缓存，则返回成功
 * 如果没缓存，则返回失败
 */
public class RequestFailedCachePolicy<T> extends BaseCachePolicy<T> {

    public RequestFailedCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        Flowable
                .concat(prepareAsyncRequestFlowable(), prepareCacheFlowable())
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
                    if (cacheEntity != null) {
                        emitter.onNext(cacheEntity.getData());
                    } else {
                        emitter.onError(new ApiException(NetError.MSG_HTTP_ERROR, NetError.NETWORD_ERROR));
                    }
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    Flowable<T> prepareAsyncRequestFlowable() {
        return super.prepareAsyncRequestFlowable()
                .onErrorResumeNext(new Publisher<T>() {
                    @Override
                    public void subscribe(Subscriber<? super T> subscriber) {
                        subscriber.onComplete();
                    }
                });
    }
}
