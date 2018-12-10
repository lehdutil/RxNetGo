package com.fungo.sample.data.api

import com.fungo.baselib.utils.EncryptUtils
import com.fungo.netgo.model.HttpParams
import com.fungo.sample.data.subscribe.NewsSubscriber
import com.fungo.sample.ui.news.NewsChannelResponse
import com.fungo.sample.ui.news.NewsContentResponse

/**
 * @author Pinger
 * @since 2018/12/10 20:30
 */
object NewsApi : BaseApi {

    private const val BASE_URL = "http://route.showapi.com/"

    override fun getBaseUrl(): String {
        return BASE_URL
    }

    private const val APP_KEY = "6610345c66364269a2df87cb7f1e3851"
    private const val APP_ID = "82658"
    private const val PAGE_SIZE = 30

    /**
     * 获取新闻内容
     */
    fun getNewsContent(channelId: String, subscriber: NewsSubscriber<NewsContentResponse>) {
        val params = HttpParams()
        params.put("channelId", channelId)
        params.put("maxResult", PAGE_SIZE)
        params.put("showapi_appid", APP_ID)
        params.put("showapi_timestamp", System.currentTimeMillis())
        val signUrl = sign(params)
        params.put("showapi_sign", signUrl)
        generateService()
                .post<NewsContentResponse>("109-35")
                .upJson(params.getUrlParams())
                .subscribe(subscriber)
    }


    /**
     * "showapi_res_code": 0,
     * "showapi_res_error": "",
     * "showapi_res_body":NewsChannelResponse
     *
     * 获取新闻频道
     */
    fun getNewsChannel(page: Int, subscriber: NewsSubscriber<NewsChannelResponse>) {
        val params = HttpParams()

        params.put("page", page)
        params.put("showapi_appid", APP_ID)
        params.put("showapi_timestamp", System.currentTimeMillis())
        val signUrl = sign(params)
        params.put("showapi_sign", signUrl)

        generateService()
                .post<NewsChannelResponse>("109-34")
                .upJson(params.getUrlParams())
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