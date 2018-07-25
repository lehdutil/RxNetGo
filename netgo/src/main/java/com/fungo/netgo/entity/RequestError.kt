package com.fungo.netgo.entity

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     自定义错误
 */

class RequestError : Throwable {
    var state: Int = 0
        private set
    var type: Int = 0//1--->本地错误，2---->服务器确定的错误代码
        private set

    constructor(state: Int, message: String) : super(message) {
        this.state = state
    }

    constructor(state: Int, message: String, type: Int) : super(message) {
        this.state = state
        this.type = type
    }

    companion object {
        val TYPE_LOCAL = 0
        val TYPE_SERVER = 1
    }

    override fun toString(): String {
        return "RequestError{" +
                "State=" + state +
                "message = " + message +
                '}'
    }
}

