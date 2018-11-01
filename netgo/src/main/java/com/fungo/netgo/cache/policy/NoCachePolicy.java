package com.fungo.netgo.cache.policy;

import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 *
 * 没有缓存，只跟网络请求有关
 *
 */
public class NoCachePolicy<T> extends BaseCachePolicy<T> {

    public NoCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        prepareAsyncRequestFlowable()
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }

}
