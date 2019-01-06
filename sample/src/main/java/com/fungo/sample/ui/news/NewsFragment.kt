package com.fungo.sample.ui.news

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.fungo.business.recycler.BaseRecyclerContract
import com.fungo.business.recycler.BaseRecyclerFragment
import com.fungo.business.recycler.item.VerticalItemSpaceDecoration

/**
 * @author Pinger
 * @since 18-12-11 下午3:08
 */
class NewsFragment : BaseRecyclerFragment() {

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


    override fun attachPresenter(): BaseRecyclerContract.Presenter = NewsPresenter(this, mChannelId)

    override fun initPageView() {
        register(NewsContentBean::class.java, NewsContentHolder())
    }


    override fun generateItemDivider(): RecyclerView.ItemDecoration? {
        return VerticalItemSpaceDecoration(36)
    }

    override fun getStartPage(): Int = 1


}