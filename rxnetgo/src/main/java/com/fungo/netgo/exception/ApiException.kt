package com.fungo.netgo.exception

import android.text.TextUtils

/**
 * @author Pinger
 * @since 18-10-17 下午4:29
 *
 *
 * 本地请求和数据发生的异常
 */
class ApiException(error: Throwable? = null, private var code: Int = 0, private var msg: String? = null) : Exception(error) {

    fun getMsg(): String? {
        return if (TextUtils.isEmpty(msg)) {
            super.message
        } else {
            msg
        }
    }


    fun setMsg(msg: String) {
        this.msg = msg
    }

    fun setCode(code: Int) {
        this.code = code
    }


    fun getCode(): Int {
        return this.code
    }

}