package com.fungo.sample.ui.read

import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.BaseNavTabFragment
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 2018/12/14 18:39
 */
class ReadMainFragment : BaseNavTabFragment() {


    override fun initData() {
        GankApi.getReadCategories(object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                val fragments = arrayListOf<BaseFragment>()
                val titles = arrayListOf<String>()

                data.results.forEach {
                    fragments.add(ReadFragment.newInstance(it.en_name))
                    titles.add(it.name)
                }
                setTabAdapter(fragments, titles)
            }
        })
    }

}