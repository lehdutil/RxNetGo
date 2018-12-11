package com.fungo.sample.ui.news

import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.sample.R
import com.fungo.sample.data.api.NewsApi
import com.fungo.sample.data.subscribe.NewsSubscriber
import com.fungo.sample.ui.fragment.BaseMainTabFragment

/**
 * @author Pinger
 * @since 18-12-7 上午11:03
 */
class NewsMainFragment : BaseMainTabFragment() {

    companion object {
        private const val FILTER_CHANNEL = "焦点"
    }


    override fun getPageTitle(): String? = getString(R.string.title_news)

    override fun initData() {
        NewsApi.getNewsChannel(object : NewsSubscriber<NewsChannelResponse>() {
            override fun onSuccess(data: NewsChannelResponse) {
                if (data.channelList != null && data.channelList.isNotEmpty()) {
                    val fragments = arrayListOf<BaseFragment>()
                    val titles = arrayListOf<String>()
                    data.channelList.forEach {
                        // 是焦点新闻
                        if (it.name.contains(FILTER_CHANNEL)) {
                            val length = it.name.length
                            val channel = it.name.substring(0, length - 2)
                            titles.add(channel)
                            fragments.add(NewsFragment.newInstance(it.channelId))
                        }
                    }

                    setTabAdapter(fragments, titles)
                }
            }
        })
    }


}