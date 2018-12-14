package com.fungo.sample.ui.read

import android.os.Bundle
import com.fungo.baseuilib.fragment.BaseContentFragment
import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.baseuilib.recycler.BaseRecyclerFragment

/**
 * @author Pinger
 * @since 2018/12/14 18:43
 */
class ReadFragment : BaseRecyclerFragment() {


    private var mId: String? = null

    companion object {

        const val READ_ID = "READ_ID"

        @JvmStatic
        fun newInstance(id: String): ReadFragment {
            val fragment = ReadFragment()
            val bundle = Bundle()
            bundle.putString(READ_ID, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mId = arguments?.getString(READ_ID)
        super.onCreate(savedInstanceState)
    }

    override fun getPresenter(): BaseRecyclerContract.Presenter = ReadPresenter(this,mId)
}