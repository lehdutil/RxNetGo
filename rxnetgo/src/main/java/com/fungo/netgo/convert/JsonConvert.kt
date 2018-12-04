package com.fungo.netgo.convert

import com.fungo.netgo.subscribe.base.IConverter
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
    override fun convertResponse(response: ResponseBody?): T {

        if (mType == null) {
            if (mClazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                val genType = javaClass.genericSuperclass
                mType = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                return parseClass(response, mClazz!!)
            }
        }

        return when (mType) {
            is Class<*> -> parseClass(response, mType as Class<T>)
            else -> parseType(response, mType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseClass(response: ResponseBody?, rawType: Class<T>): T {
        return if (response == null) {
            Any() as T
        } else {
            val json = response.string()
            response.close()
            when (rawType) {
                String::class.java -> json as T
                JSONObject::class.java -> JSONObject(json) as T
                JSONArray::class.java -> JSONArray(json) as T
                else -> JsonUtils.fromJson(json, rawType)
            }
        }


    }

    @Suppress("UNCHECKED_CAST")
    private fun parseType(response: ResponseBody?, type: Type?): T {
        if (type == null || response == null) return Any() as T
        val json = response.string()
        response.close()
        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        return JsonUtils.fromJson(json, type)
    }
}