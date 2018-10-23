package com.fungo.netgo.request;

import com.google.gson.JsonElement;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author Pinger
 * @since 18-10-18 下午3:20
 *
 * Retrofit的Service的方法上不能带有泛型，就是不能有未知类型，这里将原来的泛型改成了JsonElement,自己主动去解析数据
 *
 */
public interface FungoApi {

    @POST("{url}")
    Flowable<JsonElement> postRequest(@Path("url") String url, @Body BaseBodyRequest params);

    @POST
    Flowable<JsonElement> postRequestWithFullUrl(@Url String url, @Body BaseBodyRequest params);

    @POST("{url}")
    Flowable<JsonElement> getRequest(@Path("url") String url);

    @GET
    Flowable<JsonElement> getRequestWithFullUrl(@Url String url);

}
