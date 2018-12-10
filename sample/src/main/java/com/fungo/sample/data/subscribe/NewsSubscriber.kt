package com.fungo.sample.data.subscribe

import android.text.TextUtils
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.JsonUtils
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
        } else if (code != 0) {
            this.dispose()
            onError(ApiException(msg = error))
            onComplete()
            null
        } else null
    }

    override fun getType(): Type {
        val genType = javaClass.genericSuperclass
        return (genType as ParameterizedType).actualTypeArguments[0]
    }


}