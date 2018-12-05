package com.fungo.netgo.convert

import com.fungo.netgo.convert.base.IConverter
import okhttp3.ResponseBody

/**
 * @author Pinger
 * @since 18-12-3 下午3:41
 *
 * 字符串转换器，直接获取ResponseBody的字符串返回即可
 */
class StringConvert : IConverter<String> {

    override fun convertResponse(response: ResponseBody?): String? {
        val result = response?.string()
        response?.close()
        return result
    }
}