package com.fungo.netgo.api

import com.fungo.netgo.entity.BaseRequestInfo
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     网络请求api
 */
interface FungoApi {

    @POST("{sourceUrl}")
    fun <T> postRequest(@Path("sourceUrl") sourceUrl: String, @Body params: BaseRequestInfo): Observable<T>

    @POST
    fun <T> postRequestWithFullUrl(@Url sourceUrl: String, @Body params: BaseRequestInfo): Observable<T>

    @POST("{sourceUrl}")
    fun <T> getRequest(@Path("sourceUrl") sourceUrl: String): Observable<T>

    @GET
    fun <T> getRequestWithFullUrl(@Url sourceUrl: String): Observable<T>
}
