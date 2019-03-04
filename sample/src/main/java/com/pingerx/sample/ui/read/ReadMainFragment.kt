package com.pingerx.sample.ui.read

import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.sample.R
import com.pingerx.sample.data.api.GankApi
import com.pingerx.sample.ui.fragment.BaseMainTabFragment

/**
 * @author Pinger
 * @since 2018/12/14 18:39
 */
class ReadMainFragment : BaseMainTabFragment() {

    override fun getPageTitle(): String? = getString(R.string.title_read)

    override fun initData() {
        GankApi.getReadCategories {
            onSuccess {
                val fragments = arrayListOf<BaseFragment>()
                val titles = arrayListOf<String>()

                it.results.forEach { data ->
                    fragments.add(ReadFragment.newInstance(ReadFragment.READ_TYPE_CATEGORY, data.en_name))
                    titles.add(data.name)
                }
                setTabAdapter(fragments, titles)
                showPageContent()
            }
        }
    }


}