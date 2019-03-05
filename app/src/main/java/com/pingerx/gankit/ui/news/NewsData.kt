package com.pingerx.gankit.ui.news

/**
 * @author Pinger
 * @since 2018/12/10 20:43
 */

// 新闻频道
data class NewsChannelBean(val channelId: String, val name: String)

data class NewsChannelResponse(val totalNum: Int, val ret_code: Int, val channelList: List<NewsChannelBean>?)


// 新闻内容
data class NewsContentResponse(val ret_code: Int, val pagebean: NewsPageBean)

data class NewsPageBean(val currentPage: Int, val allNum: Int, val maxResult: Int, val allPages: Int, val contentlist: List<NewsContentBean>)

data class NewsContentBean(
        val link: String,
        val id: String,
        val channelId: String,
        val havePic: Boolean,
        val pubDate: String,
        val title: String,
        val channelName: String,
        val source: String,
        val imageurls: List<NewsImageBean>?
)

data class NewsImageBean(val height: Int, val width: Int, val url: String)