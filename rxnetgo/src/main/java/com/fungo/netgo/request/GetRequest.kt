package com.fungo.netgo.request

import com.fungo.netgo.request.base.ApiService
import com.fungo.netgo.request.base.Request

import io.reactivex.Flowable
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-10-23 下午2:11
 *
 *
 * get请求构造器
 */
class GetRequest<T>(url: String, service: ApiService?, flowable: Flowable<T>?) : Request<T>(url, service, flowable) {

    override fun generateRequestBody(): RequestBody? {
        return null
    }

    override fun getMethod(): RequestType {
        return RequestType.GET
    }
}
