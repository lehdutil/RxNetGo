package com.fungo.sample.data.api

import com.fungo.netgo.RxNetGo

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

   
    fun getNetGo(): RxNetGo {
        return RxNetGo.getInstance()
    }


}