package com.fungo.netgo.convert

import com.fungo.netgo.convert.base.IConverter
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Pinger
 * @since 18-12-3 下午3:44
 *
 * 文件转换器，将相应流中的数据保存到本地文件
 * 不支持断点续传，如果需要多文件下载并且断点续传，使用rxdownload类库
 *　TODO 做好缓存
 */
class FileConvert(
        val fileDir: String = "",
        val fileName: String = ""

) : IConverter<File> {

    override fun convertResponse(response: ResponseBody?): File {
        return File("")
    }
}