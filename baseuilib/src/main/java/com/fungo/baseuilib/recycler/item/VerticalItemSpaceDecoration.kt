package com.fungo.baseuilib.recycler.item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Pinger
 * @since 18-11-7 下午4:51
 */
class VerticalItemSpaceDecoration(
    private val itemSpace: Int,
    private val topAndBottomSpace: Int = itemSpace
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        when (itemPosition) {
            0 -> {  // 第一个
                outRect.top = topAndBottomSpace
                outRect.bottom = itemSpace / 2
            }
            state.itemCount - 1 -> {  // 最后一个
                outRect.top = itemSpace / 2
                outRect.bottom = topAndBottomSpace
            }
            else -> {
                outRect.top = itemSpace / 2
                outRect.bottom = itemSpace / 2
            }
        }
    }

}