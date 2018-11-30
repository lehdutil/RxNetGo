package com.fungo.netgo.subscribe;

import com.fungo.netgo.callback.CallBack;
import com.fungo.netgo.exception.ApiException;

/**
 * @author Pinger
 * @since 18-09-12 下午17:21
 * <p>
 * 请求订阅者，请求的各种状态都交给CallBack去处理
 */
public class RxSubscriber<T> extends BaseSubscriber<T> {

    private CallBack<T> mCallBack;

    public RxSubscriber(CallBack<T> callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected void onError(ApiException e) {
        mCallBack.onError(e);
        System.out.println("-----------> onError--------");
    }

    @Override
    protected void onStart() {
        mCallBack.onStart();
        System.out.println("-----------> onStart--------");
    }

    @Override
    public void onNext(T t) {
        mCallBack.onSuccess(t);
        System.out.println("-----------> onSuccess--------");
    }

    @Override
    public void onComplete() {
        mCallBack.onFinish();
        System.out.println("-----------> onFinish--------");
    }
}
