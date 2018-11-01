package com.fungo.netgo.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @author Pinger
 * @since 18-08-25 下午18:43
 * <p>
 * 封装请求错误的提示信息和错误码
 */
public class NetError {

    // 服务端的异常，对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    // 本地请求数据和解析等发生的异常码
    public static final int UNKNOWN = 1000;         // 未知错误
    public static final int PARSE_ERROR = 1001;     // 解析错误
    public static final int NETWORD_ERROR = 1002;   // 网络错误
    public static final int HTTP_ERROR = 1003;      // 协议出错
    public static final int ERROR_DATA = 1005;      // 数据错误
    public static final int UNKNOW_HOST = 10006;    // 域名解析异常
    public static final int NET_BREAK = 10007;      // 网络连接失败
    public static final int SERVER_ERROR = 10008;   // 服务器异常

    // 异常描述
    public static final String MSG_HTTP_ERROR = "网络错误";
    public static final String MSG_UNKNOW = "未知错误";
    public static final String MSG_PARSE_ERROR = "数据解析错误";
    public static final String MSG_NETWORD_ERROR = "连接失败";
    public static final String MSG_ERROR_DATA = "数据错误";
    public static final String MSG_UNKNOW_HOST = "域名解析异常";
    public static final String MSG_NET_BREAK = "网络连接失败";

    public static final String MSG_SERVER_ERROR = "服务器异常";

    /**
     * 分发网络请求的异常
     */
    public static ApiException handleException(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ApiException exception = new ApiException(e, HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    exception.setMessage(MSG_HTTP_ERROR);
                    break;
            }
            return exception;
        } else if (e instanceof UnknownHostException) {
            UnknownHostException unknownHostException = (UnknownHostException) e;
            return new ApiException(unknownHostException, MSG_UNKNOW_HOST, UNKNOW_HOST);
        } else if (e instanceof ServerException) {
            ServerException serverException = (ServerException) e;
            return new ApiException(serverException, serverException.message, serverException.code);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            // 解析错误
            return new ApiException(e, MSG_PARSE_ERROR, PARSE_ERROR);
        } else if (e instanceof ConnectException) {
            // 网络错误
            return new ApiException(e, MSG_NETWORD_ERROR, NETWORD_ERROR);
        } else {
            // 未知错误
            return new ApiException(e, MSG_UNKNOW, UNKNOWN);
        }
    }
}
