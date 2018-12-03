package com.fungo.netgo.convert

import android.text.TextUtils
import com.fungo.netgo.subscribe.base.IConverter
import com.fungo.netgo.utils.JsonUtils
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType

/**
 * @author Pinger
 * @since 18-12-3 下午3:47
 *
 * Json数据实体转换器，这里提供一个简单的版本，如果解析失败，请自定义解析方法
 */
class JsonConvert<T> : IConverter<T> {

    @Suppress("UNCHECKED_CAST")
    override fun convertResponse(response: ResponseBody?): T {
        val genType = javaClass.genericSuperclass
        val type = (genType as ParameterizedType).actualTypeArguments[0]

        val json = response?.string()
        response?.close()

        return if (!TextUtils.isEmpty(json)) {
            JsonUtils.fromJson(json!!, type)
        } else Any() as T
    }
}