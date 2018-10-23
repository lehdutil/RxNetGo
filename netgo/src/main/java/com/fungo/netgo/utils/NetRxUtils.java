package com.fungo.netgo.utils;

import com.fungo.netgo.error.NetError;
import com.fungo.netgo.error.ServerException;
import com.fungo.netgo.model.IModel;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Pinger
 * @since 18-10-17 上午11:23
 */
public class NetRxUtils {

    /**
     * 线程切换
     */
    public static <T extends IModel> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 请求返回后，数据正常的情况下，分发一下数据的异常
     */
    public static <T extends IModel> FlowableTransformer<T, T> getApiTransformer() {

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T model) {
                        if (model != null && model.isSuccess()) {
                            return Flowable.just(model);
                        } else {
                            if (model == null) {
                                throw new ServerException(NetError.MSG_ERROR_DATA, NetError.ERROR_DATA);
                            } else {
                                throw new ServerException(model.getErrorMsg(), model.getCode());
                            }
                        }
                    }
                }).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }


    /**
     * 响应中的异常处理
     */
    private static class HttpResponseFunc<T> implements Function<Throwable, Publisher<? extends T>> {

        @Override
        public Publisher<? extends T> apply(Throwable throwable) {
            return Flowable.error(NetError.handleException(throwable));
        }
    }

}
