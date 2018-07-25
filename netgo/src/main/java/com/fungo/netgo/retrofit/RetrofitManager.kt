package com.fungo.netgo.retrofit

import android.os.Environment
import com.fungo.netgo.api.FungoApi
import com.fungo.netgo.utils.FungoUrls
import com.fungo.netgo.utils.GsonUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author Pinger
 * @since 2018/4/12 16:33
 */

class RetrofitManager private constructor() {

    private val mFungoApi: FungoApi

    companion object {
        private var mInstance: RetrofitManager? = null
        val instance: RetrofitManager
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitManager()
                }
                return mInstance!!
            }
    }

    init {
        val cacheSize = 10 * 1024 * 1024
        val cachePath = getCachePath() + "cache.net"


        val cache = Cache(File(cachePath), cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(8, TimeUnit.SECONDS)
                .build()

        val netRetrofit = Retrofit.Builder()
                .baseUrl(FungoUrls.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        mFungoApi = netRetrofit.create(FungoApi::class.java)

        println("---RetrofitManager---> netService：$mFungoApi")
    }

    fun getFungoApi(): FungoApi {
        return mFungoApi
    }

    private fun getCachePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + File.separator
    }
}