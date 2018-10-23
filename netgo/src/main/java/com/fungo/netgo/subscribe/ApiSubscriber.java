package com.fungo.netgo.subscribe;

import com.fungo.netgo.error.ApiException;
import com.fungo.netgo.error.NetError;
import com.fungo.netgo.model.IModel;


/**
 * @author Pinger
 * @since 18-09-12 下午17:21
 * <p>
 * 请求订阅者，请求的各种状态都进行封装处理
 * 这里订阅者只能是IModel类型的实体
 */
public abstract class ApiSubscriber<T extends IModel> extends BaseSubscriber<T> {

    /**
     * 请求成功
     */
    protected abstract void onSuccess(T t);


    /**
     * 这里可以对结果进行一些处理
     */
    @Override
    final public void onNext(T t) {
        if (t.isSuccess()) {
            onSuccess(t);
        }else {
            onError(new ApiException(NetError.MSG_SERVER_ERROR,NetError.SERVER_ERROR));
        }
    }

}
