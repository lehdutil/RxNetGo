package com.fungo.sample.ui.read

import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.ui.fragment.BaseMainTabFragment
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 2018/12/14 18:39
 */
class ReadMainFragment : BaseMainTabFragment() {

    override fun getPageTitle(): String? = getString(R.string.title_read)

    override fun initData() {
        showLoading()
        GankApi.getReadCategories(object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                val fragments = arrayListOf<BaseFragment>()
                val titles = arrayListOf<String>()

                data.results.forEach {
                    fragments.add(ReadFragment.newInstance(ReadFragment.READ_TYPE_CATEGORY, it.en_name))
                    titles.add(it.name)
                }
                setTabAdapter(fragments, titles)
                showContent()
            }

            override fun onError(exception: ApiException) {
                showError()
            }
        })
    }


}