package com.pingerx.baselib.base.recycler.empty

import android.view.ViewGroup
import com.pingerx.baselib.R
import com.pingerx.baselib.base.recycler.BaseViewHolder
import com.pingerx.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.pingerx.baselib.utils.ViewUtils

/**
 * @author Pinger
 * @since 2019/2/9 14:59
 */
class MultiEmptyHolder(private val height: Int = 12, private val color: Int = 0) : MultiTypeViewHolder<RecyclerEmptyBean, MultiEmptyHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<RecyclerEmptyBean>(parent, R.layout.base_holder_empty) {
        override fun onBindView() {
            itemView.post {
                val params = itemView.layoutParams
                params.height = ViewUtils.dp2px(height)
                itemView.layoutParams = params
            }
            if (color != 0) {
                itemView.setBackgroundColor(getColor(color))
            }
        }

        override fun onBindData(data: RecyclerEmptyBean) {
        }

    }
}