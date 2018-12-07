package com.fungo.sample.ui.gank


/**
 * @author Pinger
 * @since 18-12-7 上午11:27
 *
 * Gank数据实体
 *
 */

data class GankResponse(val error: Boolean, val results: List<GankDataBean>)


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
        val images: List<String>?
)
