package com.fungo.netgo.exception;

import android.text.TextUtils;

/**
 * @author Pinger
 * @since 18-10-17 下午4:29
 * <p>
 * 本地请求和数据发生的异常
 */
public class ApiException extends Exception {

    private int code;
    private String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }


    public ApiException(Throwable throwable, String message, int code) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public ApiException( String message, int code) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return super.getMessage();
        } else {
            return message;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }



    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public static ApiException UNKNOWN() {
        return new ApiException("unknown exception!");
    }

    public static ApiException BREAKPOINT_NOT_EXIST() {
        return new ApiException("breakpoint file does not exist!");
    }

    public static ApiException BREAKPOINT_EXPIRED() {
        return new ApiException("breakpoint file has expired!");
    }

}