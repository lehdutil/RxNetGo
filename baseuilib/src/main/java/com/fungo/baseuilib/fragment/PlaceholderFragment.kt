package com.fungo.baseuilib.fragment

import android.os.Bundle
import com.fungo.baseuilib.R
import kotlinx.android.synthetic.main.base_fragment_placeholder.*


/**
 * @author Pinger
 * @since 2018/7/26 下午10:19
 *
 * 用于占位的Fragment
 */
class PlaceholderFragment : BaseNavFragment() {

    companion object {
        private const val INTENT_TITLE: String = "INTENT_TITLE"
        private const val INTENT_DESC: String = "INTENT_DESC"
        private const val INTENT_TOOL_BAR: String = "INTENT_TOOL_BAR"

        @JvmStatic
        fun newInstance(
            title: String = "暂无数据",
            description: String = "暂无数据",
            isShowToolbar: Boolean = true
        ): BaseFragment {
            val bundle = Bundle()
            bundle.putString(INTENT_TITLE, title)
            bundle.putString(INTENT_DESC, description)
            bundle.putBoolean(INTENT_TOOL_BAR, isShowToolbar)
            val fragment = PlaceholderFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentResID(): Int {
        return R.layout.base_fragment_placeholder
    }

    override fun getPageTitle(): String? {
        return arguments?.getString(INTENT_TITLE)
    }

    override fun isShowToolBar(): Boolean = arguments?.getBoolean(INTENT_TOOL_BAR) ?: true

    override fun initContentView() {
        setText(baseTvPlaceholder, arguments?.getString(INTENT_DESC))
    }
}