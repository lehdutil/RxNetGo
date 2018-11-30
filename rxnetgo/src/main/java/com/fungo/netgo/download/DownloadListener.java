package com.fungo.netgo.download;

import com.fungo.netgo.progress.ProgressListener;

import java.io.File;

/**
 * @author Pinger
 * @since 18-10-24 上午9:44
 * <p>
 * 下载进度监听，传入一个tag，知道当前监听的是哪一个任务
 */
public abstract class DownloadListener implements ProgressListener<File> {

    public final Object tag;

    public DownloadListener(Object tag) {
        this.tag = tag;
    }
}
