package com.fungo.netgo.exception;

import com.fungo.netgo.utils.HttpUtils;

import retrofit2.Response;

/**
 * @author Pinger
 * @since 18-10-24 上午9:57
 */
public class HttpException extends RuntimeException {


    private int code;                               //HTTP status code
    private String message;                         //HTTP status message
    private transient Response<?> response;         //The full HTTP response. This may be null if the exception was serialized

    public HttpException(String message) {
        super(message);
    }

    public HttpException(Response<?> response) {
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }

    private static String getMessage(Response<?> response) {
        HttpUtils.checkNotNull(response, "response == null");
        return "HTTP " + response.code() + " " + response.message();
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Response<?> response() {
        return response;
    }

    public static HttpException NET_ERROR() {
        return new HttpException("network error! http response code is 404 or 5xx!");
    }

    public static HttpException COMMON(String message) {
        return new HttpException(message);
    }

}
