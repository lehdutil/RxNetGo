package com.fungo.netgo.request.base;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Pinger
 * @since 18-10-18 下午3:20
 * <p>
 * Retrofit的Service的方法上不能带有泛型，就是不能有未知类型，这里将原来的泛型改成了ResponseBody,自己主动去处理数据
 */
public interface ApiService {


    /**
     * post异步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  请求参数
     * @param body    请求体，为RequestBody对象
     */
    @POST()
    Flowable<Response<ResponseBody>> postAsync(
            @Url() String url,
            @HeaderMap Map<String, Object> headers,
            @QueryMap Map<String, Object> params,
            @Body RequestBody body);


    /**
     * post同步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  请求参数
     * @param body    请求体，为RequestBody对象
     */
    @POST()
    Call<Response<ResponseBody>> postSync(
            @Url() String url,
            @HeaderMap Map<String, Object> headers,
            @QueryMap Map<String, Object> params,
            @Body RequestBody body);


    /**
     * get异步请求
     *
     * @param url     服务器接口
     * @param headers 请求头
     * @param params  参数
     */
    @GET()
    Flowable<Response<ResponseBody>> getAsync(
            @Url String url,
            @HeaderMap Map<String, Object> headers,
            @QueryMap Map<String, Object> params);


    /**
     * get同步请求
     *
     * @param url    服务器接口
     * @param params 参数
     */
    @GET()
    Call<Response<ResponseBody>> getSync(
            @Url String url,
            @HeaderMap Map<String, Object> headers,
            @QueryMap Map<String, Object> params);


    /**
     * 上传图片
     *
     * @param url         服务器接口
     * @param requestBody 请求体
     */
    @Multipart
    @POST()
    Flowable<Response<ResponseBody>> uploadImage(
            @Url() String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);


    /**
     * 上传文件
     *
     * @param url         服务器接口
     * @param requestBody 请求体
     * @param file        要上传的文件
     */
    @Multipart
    @POST()
    Flowable<Response<ResponseBody>> uploadFile(
            @Url String url,
            @Part("description") RequestBody requestBody,
            @Part("image\"; filename=\"image.jpg") MultipartBody.Part file);


    /**
     * 下载
     *
     * @param url 服务器接口
     */
    @Streaming
    @GET
    Flowable<Response<ResponseBody>> downloadFile(@Url String url);

}
