package com.fungo.netgo.subscribe

import android.text.TextUtils
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import com.fungo.netgo.utils.GsonUtils
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType

/**
 * @author Pinger
 * @since 18-11-30 下午6:19
 *
 * 返回结果需要转为对象的Bean，可以使用本观察者，但是并没有对数据实体进行上层的额封装，生成的是整个数据实体
 * 如果需要对数据实体进行二次封装，可以在onNext方法中进行处理
 */
abstract class JsonSubscriber<T> : BaseSubscriber<T>() {

    override fun onStart() {

    }

    override fun onError(exception: ApiException) {

    }

    override fun onComplete() {
    }


    @Suppress("UNCHECKED_CAST")
    override fun convertResponse(response: ResponseBody?): T {
        val genType = javaClass.genericSuperclass
        val type = (genType as ParameterizedType).actualTypeArguments[0]

        val json = response?.string()
        response?.close()

        return if (!TextUtils.isEmpty(json)) {
            GsonUtils.fromJson(response!!.string(), type)
        } else Any() as T
    }
}