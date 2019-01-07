package com.pingerx.sample.data.api

import com.fungo.baselib.utils.EncryptUtils
import com.pingerx.rxnetgo.model.HttpParams
import com.pingerx.sample.data.subscribe.NewsSubscriber
import com.pingerx.sample.ui.news.NewsChannelResponse
import com.pingerx.sample.ui.news.NewsContentResponse

/**
 * @author Pinger
 * @since 2018/12/10 20:30
 */
object NewsApi : BaseApi {

    private const val BASE_URL = "https://route.showapi.com/"

    override fun getBaseUrl(): String {
        return BASE_URL
    }

    private const val APP_KEY = "6610345c66364269a2df87cb7f1e3851"
    private const val APP_ID = "82658"
    private const val PAGE_SIZE = 30

    /**
     * 获取新闻列表
     */
    fun getNewsContent(page: Int, channelId: String, function: NewsSubscriber<NewsContentResponse>.() -> Unit) {
        val subscriber = object : NewsSubscriber<NewsContentResponse>() {}
        subscriber.function()

        val params = HttpParams()
        params.put("channelId", channelId)
        params.put("maxResult", PAGE_SIZE)
        params.put("page", page)
        params.put("showapi_appid", APP_ID)
        params.put("showapi_timestamp", System.currentTimeMillis())
        val signUrl = sign(params)
        params.put("showapi_sign", signUrl)
        getApi()
                .get<NewsContentResponse>("109-35")
                .cacheKey("109-35$channelId$page")
                .params(params)
                .subscribe(subscriber)
    }


    /**
     * "showapi_res_code": 0,
     * "showapi_res_error": "",
     * "showapi_res_body":NewsChannelResponse
     *
     * 获取新闻频道
     */
    fun getNewsChannel(function: NewsSubscriber<NewsChannelResponse>.() -> Unit) {
        val subscriber = object : NewsSubscriber<NewsChannelResponse>() {}
        subscriber.function()

        val params = HttpParams()

        params.put("showapi_appid", APP_ID)
        params.put("showapi_timestamp", System.currentTimeMillis())
        val signUrl = sign(params)
        params.put("showapi_sign", signUrl)

        // 因为参数在不断变化，所以不能使用默认的cacheKey

        getApi()
                .get<NewsChannelResponse>("109-34")
                .cacheKey("109-34")
                .params(params)
                .subscribe(subscriber)
    }


    override fun sign(params: HttpParams): String {
        val url = StringBuilder()
        params.getUrlParams().keys.forEach {
            url.append(it).append(params.getUrlParams()[it])
        }
        url.append(APP_KEY)
        return EncryptUtils.encodeMD5(url.toString())
    }
}