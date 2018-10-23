package com.fungo.netgo.subscribe;

/**
 * @author Pinger
 * @since 18-10-19 上午11:41
 *
 * 当请求返回的结果为String类型时，可以使用本订阅者，会handle请求过程中的异常。
 *
 */
public class StringSubscriber extends BaseSubscriber<String> {

    @Override
    public final void onNext(String s) {
        onSuccess(s);
    }

    protected void onSuccess(String json){

    }
}
