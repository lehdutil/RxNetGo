package com.fungo.netgo.repository

import com.fungo.netgo.api.FungoRequest
import com.fungo.netgo.entity.BaseEntity

/**
 * @author Pinger
 * @since 2018/4/30 22:36
 */
class NetDataStore(private val fungoRequest: FungoRequest<BaseEntity>) {

    fun loadNewsList() {
        fungoRequest.getRequest<TestData>("")
    }



}
