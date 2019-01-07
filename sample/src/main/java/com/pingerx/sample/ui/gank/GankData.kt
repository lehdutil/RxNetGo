package com.pingerx.sample.ui.gank


/**
 * @author Pinger
 * @since 18-12-7 上午11:27
 *
 * Gank数据实体
 *
 */

data class GankResponse(var error: Boolean, val results: List<GankDataBean>)


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

        // 闲读分类新增的数据类型
        val en_name: String,
        val published_at: String,
        val created_at: String,
        val name: String,
        val rank: Int,
        val icon: String,
        val id: String,
        val uid: String,
        val title: String,
        val content: String,
        val cover: String,
        val crawled: String,
        val deleted: Boolean,
        val site: GankSiteBean
)

data class GankSiteBean(
        val cat_cn: String,
        val cat_en: String,
        val desc: String,
        val feed_id: String,
        val icon: String,
        val id: String,
        val name: String,
        val subscribers: Int,
        val type: String,
        val url: String
)