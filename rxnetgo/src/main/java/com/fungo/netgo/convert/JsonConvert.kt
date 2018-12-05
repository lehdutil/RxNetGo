package com.fungo.netgo.convert

import android.text.TextUtils
import com.fungo.netgo.convert.base.IConverter
import com.fungo.netgo.utils.JsonUtils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:47
 *
 * Json数据实体转换器，这里提供一个简单的版本，如果解析失败，请自定义解析方法
 * 而且每种数据结构不同，可能有些需要做封装，可以继承本类，重写[convertResponse]方法，重新解析数据
 * 使用请查看[com.fungo.netgo.subscribe.JsonSubscriber]
 */
class JsonConvert<T> : IConverter<T> {

    private var mType: Type? = null
    private var mClazz: Class<T>? = null

    constructor()

    constructor(type: Type) {
        this.mType = type
    }

    constructor(clazz: Class<T>) {
        this.mClazz = clazz
    }

    @Suppress("UNCHECKED_CAST")
    override fun convertResponse(response: ResponseBody?): T? {

        val json = response?.string()
        response?.close()

        if (TextUtils.isEmpty(json)) return null

        if (mType == null) {
            if (mClazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                val genType = javaClass.genericSuperclass
                mType = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                return parseClass(json!!, mClazz!!)
            }
        }

        return when (mType) {
            is Class<*> -> parseClass(json!!, mType as Class<T>)
            else -> parseType(json!!, mType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseClass(result: String, rawType: Class<T>): T? {
        return when (rawType) {
            String::class.java -> result as T
            JSONObject::class.java -> JSONObject(result) as T
            JSONArray::class.java -> JSONArray(result) as T
            else -> JsonUtils.fromJson(result, rawType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseType(result: String, type: Type?): T? {
        if (type == null) return null
        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        return JsonUtils.fromJson(result, type)
    }
}