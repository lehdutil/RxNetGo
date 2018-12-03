package com.fungo.netgo.subscribe

import com.fungo.netgo.convert.FileConvert
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Pinger
 * @since 18-12-3 下午3:50
 *
 * 文件订阅者，生成本地文件
 *
 */
abstract class FileSubscriber : BaseSubscriber<File>() {

    private val mConverter: FileConvert = FileConvert()

    override fun onComplete() {
    }

    override fun onError(exception: ApiException) {
    }

    override fun convertResponse(response: ResponseBody?): File {
        return mConverter.convertResponse(response)
    }
}