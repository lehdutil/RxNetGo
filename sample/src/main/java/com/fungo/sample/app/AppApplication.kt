package com.fungo.sample.app

import android.app.Application
import com.fungo.netgo.RxNetGo

/**
 * @author Pinger
 * @since 18-12-6 下午5:47
 */
class AppApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        RxNetGo.getInstance().init(this)
    }

}