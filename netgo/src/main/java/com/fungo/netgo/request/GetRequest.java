package com.fungo.netgo.request;

import com.fungo.netgo.request.base.ApiService;
import com.fungo.netgo.request.base.Request;

import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午2:11
 * <p>
 * get请求构造器
 */
public class GetRequest<T> extends Request<T, GetRequest<T>> {

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    public GetRequest(String url, ApiService service) {
        super(url, service);
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }

}
