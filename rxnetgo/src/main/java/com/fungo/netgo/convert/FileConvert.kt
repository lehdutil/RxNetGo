package com.fungo.netgo.convert

import com.fungo.netgo.convert.base.IConverter
import com.fungo.netgo.utils.HttpUtils
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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


    private var outStream: FileOutputStream? = null
    private var inStream: InputStream? = null

    private var total: Long = 0
    private var updateCount = 0
    private var interval = 1f
    private var progress = 0f


    override fun convertResponse(response: ResponseBody?): File {

        val file = HttpUtils.createDownloadFile(fileDir, fileName)

        if (response?.byteStream() == null) {
            return file
        }
        val byteStream = response.byteStream()
        response.close()

        val buf = ByteArray(2048)
        var len = 0
        var outStream: FileOutputStream? = null
        var inStream: InputStream? = null
        try {
            inStream = response.byteStream()
            val total = response.contentLength()
            interval = if (total < 2048) 0.2f else 1f

            outStream = FileOutputStream(file)





            while ((len = inStream.read(buf)) != -1) {
                total += len.toLong()
                outStream!!.write(buf, 0, len)
                val finalSum = total
                if (total == -1 || total == 0L) {
                    progress = 100
                } else {
                    progress = (finalSum * 100 / total).toInt()
                }
                if (updateCount == 0 || progress >= updateCount) {
                    updateCount += interval
                    //handler = Handler(Looper.getMainLooper())
                    // val finalProgress = progress
                    // handler.post(Runnable { onProgress(tag, finalProgress, finalSum, total) })
                }
            }
            fos!!.flush()
            return file

        } finally {
            //onRelease()
        }




        return File("")
    }


    fun onProgress(tag: Any, progress: Float, downloaded: Long, total: Long) {}

}