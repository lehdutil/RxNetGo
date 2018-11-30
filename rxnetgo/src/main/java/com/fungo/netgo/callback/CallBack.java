package com.fungo.netgo.callback;

import com.fungo.netgo.exception.ApiException;
import com.fungo.netgo.progress.Progress;

/**
 * @author Pinger
 * @since 18-10-23 上午11:42
 * <p>
 * 网络请求的回调
 */
public interface CallBack<T> extends Converter<T> {

    /**
     * 请求网络开始前，UI线程
     */
    void onStart();

    /**
     * 对返回数据进行操作的回调， UI线程
     */
    void onSuccess(T t);

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    void onError(ApiException exception);

    /**
     * 请求网络结束后，UI线程
     */
    void onFinish();

    /**
     * 上传过程中的进度回调，get请求不回调，UI线程
     */
    void uploadProgress(Progress progress);

    /**
     * 下载过程中的进度回调，UI线程
     */
    void downloadProgress(Progress progress);
}
