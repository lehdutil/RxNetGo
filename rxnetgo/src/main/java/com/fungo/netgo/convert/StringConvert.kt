package com.fungo.netgo.convert

import android.text.TextUtils
import com.fungo.netgo.subscribe.base.IConverter
import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-12-3 下午3:41
 *
 * 字符串转换器
 */
class StringConvert : IConverter<String> {

    override fun convertResponse(response: ResponseBody?): String {
        val result = response?.string()
        response?.close()
        return if (!TextUtils.isEmpty(result)) {
            result!!
        } else {
            ""
        }
    }
}