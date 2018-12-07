package com.fungo.baseuilib.recycler.item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Pinger
 * @since 18-11-7 下午4:56
 *
 * 竖直的瀑布列表
 */
class GridItemSpaceDecoration(
    private val spanCount: Int,
    private val itemSpace: Int,
    private val topAndBottomSpace: Int = itemSpace
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view) // item position
        val column = position % spanCount // item column

        outRect.left = itemSpace - column * itemSpace / spanCount // spacing - column * ((1f / spanCount) * spacing)
        outRect.right = (column + 1) * itemSpace / spanCount // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
            outRect.top = topAndBottomSpace
        } else {
            outRect.top = itemSpace / 2
        }

        if (position == parent.itemDecorationCount - 1) {
            outRect.bottom = topAndBottomSpace // item bottom
        } else {
            outRect.bottom = itemSpace / 2 // item bottom
        }
    }

}