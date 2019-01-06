package com.fungo.sample.ui.gank

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fungo.business.recycler.BaseRecyclerContract
import com.fungo.business.recycler.BaseRecyclerFragment
import com.fungo.business.recycler.item.DividerItemDecoration
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi


/**
 * @author Pinger
 * @since 18-12-7 上午11:10
 *
 * 干货集中营列表展示页面
 * https://gank.io/api
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

    override fun attachPresenter(): BaseRecyclerContract.Presenter = GankDataPresenter(this, mGankType)

    override fun initPageView() {
        register(GankDataBean::class.java, GankDataHolder())
    }

    override fun generateItemDivider(): RecyclerView.ItemDecoration? {
        return DividerItemDecoration(ContextCompat.getColor(context!!, R.color.grey_f2), height = 18)
    }


    override fun getStartPage(): Int = 1
}