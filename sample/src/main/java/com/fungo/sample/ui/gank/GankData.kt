package com.fungo.sample.ui.gank


/**
 * @author Pinger
 * @since 18-12-7 上午11:27
 *
 * Gank数据实体
 *
 */

data class GankResponse(var error: Boolean, var results: List<GankDataBean>)


data class GankDataBean(
        val _id: String,
        val createdAt: String,
        val desc: String,
        val publishedAt: String,
        val source: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String,
        val images: List<String>?,
        var height: Int = 0 // 图片的高度，仅福利类型才有
)
