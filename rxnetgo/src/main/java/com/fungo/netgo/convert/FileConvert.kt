package com.fungo.netgo.convert

import com.fungo.netgo.convert.base.IConverter
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Pinger
 * @since 18-12-3 下午3:44
 *
 * TODO
 * 文件转换器，将相应流中的数据保存到本地文件
 * 一般用于下载
 *
 */
class FileConvert : IConverter<File> {

    override fun convertResponse(response: ResponseBody?): File {
        return File("")
    }
}