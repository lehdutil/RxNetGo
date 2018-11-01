package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Pinger
 * @since 18-10-25 上午10:07
 */
public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {

    public DefaultCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        prepareAsyncRequestFlowable()
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }

    /**
     * 构建缓存加载观察者
     */
    protected Flowable<T> prepareCacheFlowable() {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) {
                if (!emitter.isCancelled()) {
                    CacheEntity<T> cacheEntity = prepareCache();
                    if (cacheEntity != null) {
                        emitter.onNext(cacheEntity.getData());
                    }
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }


    /**
     * 构建异步网络请求加载观察者
     */
    Flowable<T> prepareAsyncRequestFlowable() {
        Flowable<Response<ResponseBody>> requestFlowable = null;
        switch (mRequest.getMethod()) {
            case GET:
                requestFlowable = mRequest.getAsync();
                break;
            case POST:
                requestFlowable = mRequest.postAsync();
                break;
        }

        Flowable<T> resultFlowable = null;
        if (requestFlowable != null) {
            resultFlowable = requestFlowable
                    .flatMap(new Function<Response<ResponseBody>, Publisher<T>>() {
                        @Override
                        public Publisher<T> apply(Response<ResponseBody> response) throws Exception {
                            T t = mRequest.getCallBack().convertResponse(response.body());
                            saveCache(response.headers(), t);
                            return Flowable.just(t);
                        }
                    }).onErrorResumeNext(new Publisher<T>() {
                        @Override
                        public void subscribe(Subscriber<? super T> subscriber) {
                            subscriber.onComplete();
                        }
                    });
        }

        return resultFlowable;
    }

}
