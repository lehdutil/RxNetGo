package com.fungo.netgo.upload;

import com.fungo.netgo.progress.ProgressListener;

/**
 * @author Pinger
 * @since 18-10-24 上午10:44
 * <p>
 * 上传进度监听
 */
public abstract class UploadListener<T> implements ProgressListener<T> {

    public final Object tag;

    public UploadListener(Object tag) {
        this.tag = tag;
    }
}
