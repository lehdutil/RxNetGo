package com.pingerx.gankit.ui.news

import com.fungo.baselib.base.fragment.BaseFragment
import com.pingerx.gankit.R
import com.pingerx.gankit.data.api.NewsApi
import com.pingerx.gankit.ui.fragment.BaseMainTabFragment

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
        NewsApi.getNewsChannel {
            onSuccess {
                if (it.channelList != null && it.channelList.isNotEmpty()) {
                    val fragments = arrayListOf<BaseFragment>()
                    val titles = arrayListOf<String>()
                    it.channelList.forEach { data ->
                        // 是焦点新闻
                        if (data.name.contains(FILTER_CHANNEL)) {
                            val length = data.name.length
                            val channel = data.name.substring(0, length - 2)
                            titles.add(channel)
                            fragments.add(NewsFragment.newInstance(data.channelId))
                        }
                    }
                    setTabAdapter(fragments, titles)
                    showPageContent()
                } else {
                    showPageEmpty()
                }
            }
            onFailed { showPageError() }
        }
    }


}