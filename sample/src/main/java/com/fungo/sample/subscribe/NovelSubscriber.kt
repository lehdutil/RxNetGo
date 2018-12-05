package com.fungo.sample.subscribe

import android.text.TextUtils
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.netgo.utils.JsonUtils
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author Pinger
 * @since 18-12-5 下午3:29
 */
abstract class NovelSubscriber<T> : JsonSubscriber<T>() {


    @Suppress("UNCHECKED_CAST")
    override fun convertResponse(response: ResponseBody?): T? {
        var json = response?.string()
        response?.close()

        json = null

        return if (TextUtils.isEmpty(json)) {
            null
        } else {
            val jobj = JSONObject(json!!)
            val status = jobj.optInt("status")
            val info = jobj.optString("info")
            val data = jobj.optString("data")
            if (status != 1 || info != "success") {
                throw ApiException(msg = "服务器异常")
            } else {
                JsonUtils.fromJson(data, getType())
            }
        }
    }


}