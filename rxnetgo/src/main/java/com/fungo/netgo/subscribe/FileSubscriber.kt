package com.fungo.netgo.subscribe

import com.fungo.netgo.convert.FileConvert
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.io.File
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:50
 *
 * 文件订阅者，生成本地文件
 *
 * @param fileDir 文件下载的目录
 * @param fileName 文件名
 * @param isDelete 是否删除已经存在的文件，默认删除
 *
 */
abstract class FileSubscriber(
        fileDir: String? = null,
        fileName: String? = null,
        isDelete: Boolean = true
) : BaseSubscriber<File>() {

    private val mConvert = FileConvert(fileDir, fileName, isDelete)

    final override fun convertResponse(body: ResponseBody?): File {
        mConvert.setSubscribe(this)
        return mConvert.convertResponse(body)
    }

    final override fun getType(): Type {
        return File::class.java
    }

    /**
     * 文件下载的进度
     * @param progress 百分比，0-100，下载的进度
     * @param downloaded 已下载多少字节
     * @param total 总共多少字节
     */
    fun onProgress(progress: Int, downloaded: Long, total: Long) {}
}