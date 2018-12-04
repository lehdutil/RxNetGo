package com.fungo.netgo.subscribe

import com.fungo.netgo.convert.JsonConvert
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-11-30 下午6:19
 *
 * 返回结果需要转为对象的Bean，可以使用本观察者，但是并没有对数据实体进行上层的额封装，生成的是整个数据实体
 * 如果需要对数据实体进行二次封装，可以在onNext方法中进行处理
 */
abstract class JsonSubscriber<T> : BaseSubscriber<T> {

    private var mType: Type? = null
    private var mClazz: Class<T>? = null

    constructor()

    constructor(type: Type) {
        this.mType = type
    }

    constructor(clazz: Class<T>) {
        this.mClazz = clazz
    }


    override fun onStart() {

    }

    override fun onError(exception: ApiException) {

    }

    override fun onComplete() {
    }


    final override fun convertResponse(response: ResponseBody?): T {
        if (mType == null) {
            if (mClazz == null) {
                val genType = javaClass.genericSuperclass
                mType = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                val convert = JsonConvert(mClazz!!)
                return convert.convertResponse(response)
            }
        }

        val converter = JsonConvert<T>(mType!!)
        return converter.convertResponse(response)
    }
}