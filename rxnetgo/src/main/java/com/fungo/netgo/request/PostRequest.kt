package com.fungo.netgo.request

import com.fungo.netgo.cache.CacheApiProvider
import com.fungo.netgo.request.base.ApiService
import com.fungo.netgo.request.base.BodyRequest

import io.reactivex.Flowable

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 *
 * 构造Post请求，请求体封装请查看父类[com.fungo.netgo.request.base.BodyRequest]
 */
class PostRequest<T>(url: String, service: ApiService, cacheProvider: CacheApiProvider?, flowable: Flowable<T>?) : BodyRequest<T>(url, service, cacheProvider, flowable) {

    override fun getMethod(): RequestType {
        return RequestType.POST
    }
}
