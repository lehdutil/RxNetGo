package com.fungo.sample.data.api

import com.fungo.netgo.RxNetGo
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 *
 * Gank集中营的数据API
 * 数据提供者
 */
object GankApi : BaseApi {

    private const val BASE_GANK_URL = "http://gank.io/api/"

    private const val PAGE_SIZE = 30

    // 请求参数，资源类型
    const val GANK_TYPE_ANDROID = "Android"
    const val GANK_TYPE_IOS = "iOS"
    const val GANK_TYPE_WEB = "前端"
    const val GANK_TYPE_APP = "App"

    const val GANK_TYPE_WELFARE = "福利"
    const val GANK_TYPE_VIDEO = "休息视频"
    const val GANK_TYPE_EXPAND = "拓展资源"
    const val GANK_TYPE_ALL = "all"


    override fun getRxNetGo(): RxNetGo {
        return RxNetGo.getInstance()
    }


    override fun sign(url: String): String {
        return url
    }


    override fun generateService(): RxNetGo {
        return getRxNetGo().getRetrofitService(BASE_GANK_URL)
    }


    /**
     * 获取每日最新
     */
    fun getGankToday() {
        val url = "today"
    }


    /**
     * 获取Gank列表数据
     */
    fun getGankData(gankType: String, page: Int, subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("data/$gankType/$PAGE_SIZE/$page")
                .subscribe(subscriber)
    }


}