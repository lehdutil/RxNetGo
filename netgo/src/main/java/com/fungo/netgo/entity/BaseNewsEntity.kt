package com.fungo.netgo.entity

import com.fungo.netgo.constant.ErrorCode
import com.fungo.netgo.entity.BaseEntity
import com.google.gson.JsonElement

/**
 * @author Pinger
 * @since 2018/4/12 18:36
 */
class BaseNewsEntity(private val data:JsonElement,private val code: Int, val msg: String) : BaseEntity {

    override fun getData(): JsonElement? {
        return data
    }

    override fun isSuccess(): Boolean {
        return code == ErrorCode.SERVER_SUCCESS
    }

    override fun getMessage(): String? {
        return msg
    }

    override fun getCode(): Int {
        return code
    }
}