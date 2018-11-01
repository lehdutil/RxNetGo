package com.fungo.netgo.exception;

/**
 * @author Pinger
 * @since 18-10-17 下午4:30
 * <p>
 * 服务端发生的异常，一般为运行时异常
 */
public class ServerException extends RuntimeException {

    public int code;
    public String message;

    public ServerException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}