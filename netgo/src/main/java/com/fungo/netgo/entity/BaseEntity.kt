package com.fungo.netgo.entity

import com.google.gson.JsonElement

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     服务器返回数据结构
 */
 interface BaseEntity {

    fun getData():JsonElement?
    fun isSuccess(): Boolean
    fun getMessage(): String?
    fun getCode(): Int
}
