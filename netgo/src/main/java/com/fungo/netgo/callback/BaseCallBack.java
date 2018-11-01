package com.fungo.netgo.callback;

import com.fungo.netgo.exception.ApiException;
import com.fungo.netgo.progress.Progress;

/**
 * @author Pinger
 * @since 18-10-23 上午11:53
 * <p>
 * 回调适配器
 */
public abstract class BaseCallBack<T> implements CallBack<T> {

    @Override
    public void onStart() {

    }


    @Override
    public void onError(ApiException exception) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }
}
