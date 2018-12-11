package com.fungo.sample.ui.explore

import com.fungo.baseuilib.fragment.BaseContentFragment
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 18-12-7 上午11:03
 *
 * 发现福利页面，调用的是干货集中营福利数据
 * https://gank.io/api/data/福利/10/1
 */
class ExploreFragment : BaseContentFragment() {

    override fun getContentResID(): Int = R.layout.fragment_explore

    override fun initContentView() {

    }


    override fun initData() {
        GankApi.getGankData(GankApi.GANK_TYPE_WELFARE, 0, object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {

            }
        })
    }

}