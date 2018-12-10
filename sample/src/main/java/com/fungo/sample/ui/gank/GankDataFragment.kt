package com.fungo.sample.ui.gank

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fungo.baseuilib.R
import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.baseuilib.recycler.BaseRecyclerFragment
import com.fungo.baseuilib.recycler.item.DividerItemDecoration
import com.fungo.baseuilib.recycler.item.GridItemSpaceDecoration
import com.fungo.sample.data.api.GankApi


/**
 * @author Pinger
 * @since 18-12-7 上午11:10
 */
class GankDataFragment : BaseRecyclerFragment() {

    override fun isShowToolBar(): Boolean = false

    private var mGankType = GankApi.GANK_TYPE_ALL

    companion object {
        const val GANK_TYPE = "GANK_TYPE"

        fun newInstance(gankType: String): GankDataFragment {
            val fragment = GankDataFragment()
            val bundle = Bundle()
            bundle.putString(GANK_TYPE, gankType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mGankType = arguments?.getString(GANK_TYPE) ?: GankApi.GANK_TYPE_ALL
        super.onCreate(savedInstanceState)
    }

    override fun getPresenter(): BaseRecyclerContract.Presenter = GankDataPresenter(this, mGankType)

    override fun initPageView() {
        register(GankDataBean::class.java, GankDataHolder(mGankType))
    }

    override fun generateItemDivider(): RecyclerView.ItemDecoration? {
        return if (mGankType == GankApi.GANK_TYPE_WELFARE) {
            GridItemSpaceDecoration(2, 18)
        } else {
            DividerItemDecoration(ContextCompat.getColor(context!!, R.color.grey_f2), height = 18)
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return if (mGankType == GankApi.GANK_TYPE_WELFARE) {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else super.getLayoutManager()
    }

    override fun getStartPage(): Int = 1
}