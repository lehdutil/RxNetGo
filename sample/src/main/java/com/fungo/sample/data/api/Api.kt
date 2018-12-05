package com.fungo.sample.data.api

import com.fungo.netgo.RxNetGo
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.bean.GankBean
import com.fungo.sample.data.bean.NovelDetailBean
import com.fungo.sample.data.bean.NovelRankBean
import com.fungo.sample.subscribe.NovelSubscriber

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 */
object Api {

    private const val BASE_GANK_URL = "http://gank.io/api/"
    private const val BASE_NOVEL_URL = "http://api.zhuishushenqi.com/"
    private const val BASE_NOVEL_DETAIL_URL = "https://quapp.1122dh.com/"

    fun getNetGo(): RxNetGo {
        return RxNetGo.getInstance()
    }


    fun getApiService(): RxNetGo {
        return getNetGo().getRetrofitService(BASE_GANK_URL)
    }


    fun getNovelService(): RxNetGo {
        return getNetGo().getRetrofitService(BASE_NOVEL_URL)
    }

    fun getNovelDetailService(): RxNetGo {
        return getNetGo().getRetrofitService(BASE_NOVEL_DETAIL_URL)
    }

    fun getGankService(): GankSevice {
        return getNetGo().getRetrofitService(BASE_GANK_URL, GankSevice::class.java)
    }


    fun getGankData(subscriber: JsonSubscriber<GankBean>) {
//        getNetGo()[getGankService().getGankData()].subscribe(subscriber)
        getApiService()
                .get<GankBean>("data/Android/30/1")
                .subscribe(subscriber)
    }


    fun getNovelRankData(subscriber: JsonSubscriber<NovelRankBean>) {
        getNovelService()
                .get<NovelRankBean>("ranking/gender")
                .subscribe(subscriber)
    }


    fun getNovelDetailData(subscriber: NovelSubscriber<NovelDetailBean>) {
        getNovelDetailService()
                .get<NovelDetailBean>("Categories/1/hot/1.html")
                .subscribe(subscriber)
    }

}