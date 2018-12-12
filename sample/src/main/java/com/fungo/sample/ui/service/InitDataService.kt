package com.fungo.sample.ui.service

import android.app.IntentService
import android.content.Intent

/**
 * @author Pinger
 * @since 2018/12/12 21:28
 */
class InitDataService : IntentService("InitDataService") {

    // 子线程运行，执行完Service会自动关闭
    override fun onHandleIntent(intent: Intent?) {
    }
}