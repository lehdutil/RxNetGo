package com.pingerx.sample.ui.gank

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fungo.baselib.base.recycler.BaseMultiRecyclerFragment
import com.fungo.baselib.base.recycler.decorate.DividerItemDecoration
import com.fungo.sample.R
import com.pingerx.sample.data.api.GankApi


/**
 * @author Pinger
 * @since 18-12-7 上午11:10
 *
 * 干货集中营列表展示页面
 * https://gank.io/api
 */
class GankDataFragment : BaseMultiRecyclerFragment<GankDataPresenter>() {

    override fun attachPresenter(): GankDataPresenter = GankDataPresenter(mGankType)

    override fun attachView() {
        mPresenter.attachView(this)
    }

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


    override fun initPageView() {
        register(GankDataBean::class.java, GankDataHolder())
    }

    override fun getItemDivider(): RecyclerView.ItemDecoration? {
        return DividerItemDecoration(ContextCompat.getColor(context!!, R.color.grey_f2), height = 18)
    }
}