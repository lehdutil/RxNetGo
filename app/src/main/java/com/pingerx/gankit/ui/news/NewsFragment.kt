package com.pingerx.gankit.ui.news

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.fungo.baselib.base.recycler.BaseMultiRecyclerFragment
import com.fungo.baselib.base.recycler.decorate.VerticalItemSpaceDecoration

/**
 * @author Pinger
 * @since 18-12-11 下午3:08
 */
class NewsFragment : BaseMultiRecyclerFragment<NewsPresenter>() {

    override fun attachView() {
        mPresenter.attachView(this)
    }

    private var mChannelId: String? = null

    companion object {

        private const val CHANNEL_ID = "CHANNEL_ID"

        @JvmStatic
        fun newInstance(channelId: String): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString(CHANNEL_ID, channelId)
            fragment.arguments = bundle
            return fragment

        }
    }

    override fun isShowToolBar(): Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        mChannelId = arguments?.getString(CHANNEL_ID)
        super.onCreate(savedInstanceState)
    }

    override fun attachPresenter() = NewsPresenter(mChannelId)

    override fun initPageView() {
        register(NewsContentBean::class.java, NewsContentHolder())
    }

    override fun getItemDivider(): RecyclerView.ItemDecoration? {
        return VerticalItemSpaceDecoration(36)
    }
}