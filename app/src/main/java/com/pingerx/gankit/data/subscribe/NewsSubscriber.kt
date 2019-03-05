package com.pingerx.gankit.data.subscribe

import android.text.TextUtils
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.exception.NetErrorEngine
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import com.pingerx.gankit.utils.JsonUtils
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2018/12/10 22:36
 */
abstract class NewsSubscriber<T> : BaseSubscriber<T>() {

    override fun convertResponse(response: ResponseBody?): T? {
        val json = response?.string()
        response?.close()
        if (TextUtils.isEmpty(json)) return null
        val jobj = JSONObject(json)
        val code = jobj.optInt("showapi_res_code")
        val error = jobj.optString("showapi_res_error")
        val result = jobj.optString("showapi_res_body")
        return if (code == 0 && !TextUtils.isEmpty(result)) {
            JsonUtils.fromJson(result, getType())
        } else throw ApiException(code = NetErrorEngine.DATA_ERROR)
    }

    override fun getType(): Type {
        val genType = javaClass.genericSuperclass
        return (genType as ParameterizedType).actualTypeArguments[0]
    }


}