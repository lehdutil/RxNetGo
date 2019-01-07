package com.fungo.sample.data.api

import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 18-10-23 上午9:27
 *
 * Gank集中营的数据API
 *
 * 包括闲读的数据
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

    override fun getBaseUrl(): String {
        return BASE_GANK_URL
    }

    /**
     * 获取Gank的福利图片
     */
    fun getWelfareData(page: Int, subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("data/$GANK_TYPE_WELFARE/$PAGE_SIZE/$page")
                .cacheKey(GANK_TYPE_WELFARE + page)
                .subscribe(subscriber)
    }


    /**
     * 获取Gank列表数据
     */
    fun getGankData(gankType: String, page: Int, subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("data/$gankType/$PAGE_SIZE/$page")
                .subscribe(subscriber)
    }


    /**
     * 获取闲读的主分类
     */
    fun getReadCategories(subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("xiandu/categories")
                .subscribe(subscriber)
    }


    /**
     * 获取闲读分类子列表
     */
    fun getReadCategory(category: String, subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("xiandu/category/$category")
                .subscribe(subscriber)
    }


    /**
     * 获取闲读数据
     */
    fun getReadData(id: String, page: Int, subscriber: JsonSubscriber<GankResponse>) {
        generateService()
                .get<GankResponse>("xiandu/data/id/$id/count/$PAGE_SIZE/page/$page")
                .subscribe(subscriber)

    }

}