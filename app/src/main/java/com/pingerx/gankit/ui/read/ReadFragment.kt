package com.pingerx.gankit.ui.read

import android.os.Bundle
import com.fungo.baselib.base.recycler.BaseMultiRecyclerFragment
import com.pingerx.gankit.ui.gank.GankDataBean

/**
 * @author Pinger
 * @since 2018/12/14 18:43
 */
class ReadFragment : BaseMultiRecyclerFragment<ReadPresenter>() {

    override fun attachView() {
        mPresenter.attachView(this)
    }

    private var mCategoryId: String? = null
    private var mReadType: Int = READ_TYPE_CATEGORY

    companion object {
        private const val CATEGORY_ID = "CATEGORY_ID"
        private const val READ_DATA_TYPE = "READ_DATA_TYPE"
        private const val READ_TITLE = "READ_TITLE"

        const val READ_TYPE_CATEGORY = 1
        const val READ_TYPE_CONTENT = 2

        @JvmStatic
        fun newInstance(readType: Int, id: String, title: String? = null): ReadFragment {
            val fragment = ReadFragment()
            val bundle = Bundle()
            bundle.putInt(READ_DATA_TYPE, readType)
            bundle.putString(CATEGORY_ID, id)
            bundle.putString(READ_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mCategoryId = arguments?.getString(CATEGORY_ID)
        mReadType = arguments?.getInt(READ_DATA_TYPE) ?: READ_TYPE_CATEGORY
        super.onCreate(savedInstanceState)
    }

    override fun attachPresenter() = ReadPresenter(mReadType, mCategoryId)

    override fun initPageView() {
        if (isContentType()) {
            setPageTitle(arguments?.getString(READ_TITLE))
        }
        register(GankDataBean::class.java, ReadDataHolder(mReadType))
    }

    override fun isEnableLoadMore(): Boolean {
        return isContentType()
    }

    override fun isVerticalScrollBarEnabled(): Boolean {
        return isContentType()
    }

    override fun isShowToolBar(): Boolean = isContentType()

    override fun getStartPage(): Int = 1

    override fun isSwipeBackEnable(): Boolean {
        return isContentType()
    }

    private fun isContentType(): Boolean {
        return mReadType == READ_TYPE_CONTENT
    }

}