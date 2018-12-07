package com.fungo.baseuilib.recycler.item

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.*
import java.util.*

/**
 * @author Pinger
 * @since 18-11-22 上午10:46
 *
 * 线性分割线
 */
class DividerItemDecoration(
    val color: Int = Color.DKGRAY,
    val height: Int = 3,
    paddingLeft: Int = 0,
    paddingRight: Int = 0,
    backgroundColor: Int = Color.TRANSPARENT
) : RecyclerView.ItemDecoration() {

    private val mBackgroundColorDrawable: ColorDrawable = ColorDrawable(backgroundColor)
    private var mDividerColorDrawable: ColorDrawable = ColorDrawable(color)
    private var mHeight: Int = height
    private val mPaddingLeft: Int = paddingLeft
    private val mPaddingRight: Int = paddingRight
    private var mDrawLastItem = true
    private var mDrawHeaderFooter = false
    private var mDividerFilterList: MutableList<DividerFilter>? = null

    private var mDrawFirstDivider = false
    private var mDrawLastDivider = false


    fun setDrawFirstDivider(isDraw: Boolean) {
        this.mDrawFirstDivider = isDraw
    }

    fun setDrawLatDivider(isDraw: Boolean) {
        this.mDrawLastDivider = isDraw
    }

    fun setDrawLastItem(mDrawLastItem: Boolean) {
        this.mDrawLastItem = mDrawLastItem
    }

    fun setDrawHeaderFooter(mDrawHeaderFooter: Boolean) {
        this.mDrawHeaderFooter = mDrawHeaderFooter
    }

    fun addDividerFilter(filter: DividerFilter?) {
        if (filter == null) {
            return
        }
        if (mDividerFilterList == null) {
            mDividerFilterList = ArrayList()
        }
        mDividerFilterList!!.add(filter)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        var orientation = 0

        val layoutManager = parent.layoutManager
        when (layoutManager) {
            is StaggeredGridLayoutManager -> orientation = layoutManager.orientation
            is GridLayoutManager -> orientation = layoutManager.orientation
            is LinearLayoutManager -> orientation = layoutManager.orientation
        }

        var dividerHeight = mHeight

        // 分割线过滤，被过滤的位置不绘制分割线

        // 分割线过滤，被过滤的位置不绘制分割线
        if (mDividerFilterList != null) {
            var skip = false
            for (filter in mDividerFilterList!!) {
                if (filter.skipDraw(position)) {
                    skip = true
                    break
                }
            }
            if (skip) {
                dividerHeight = 0
            }
        }

        if (position >= 0 && position < parent.adapter!!.itemCount || mDrawHeaderFooter) {
            if (orientation == OrientationHelper.VERTICAL) {
                if (position == 0 && mDrawFirstDivider) {
                    outRect.top = dividerHeight
                }
                if (position != parent.adapter!!.getItemCount() - 1 || mDrawLastDivider) {
                    outRect.bottom = dividerHeight
                }
            } else {
                if (position == 0 && mDrawFirstDivider) {
                    outRect.left = dividerHeight
                }
                if (position != parent.adapter!!.getItemCount() - 1 || mDrawLastDivider) {
                    outRect.right = dividerHeight
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.adapter == null) {
            return
        }

        var orientation = 0
        val dataCount: Int = parent.adapter!!.itemCount

        val layoutManager = parent.layoutManager
        //数据项除了最后一项
        //数据项最后一项
        when (layoutManager) {
            is StaggeredGridLayoutManager -> orientation = layoutManager.orientation
            is GridLayoutManager -> orientation = layoutManager.orientation
            is LinearLayoutManager -> orientation = layoutManager.orientation
        }
        val start: Int
        val end: Int
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.paddingLeft + mPaddingLeft
            end = parent.width - parent.paddingRight - mPaddingRight
        } else {
            start = parent.paddingTop + mPaddingLeft
            end = parent.height - parent.paddingBottom - mPaddingRight
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position >= 0 && position < dataCount - 1//数据项除了最后一项

                || position == dataCount - 1 && mDrawLastItem//数据项最后一项

                || position !in 0..(dataCount - 1) && mDrawHeaderFooter
            ) {

                if (orientation == OrientationHelper.VERTICAL) {
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + mHeight
                    if (start > 0) {
                        mBackgroundColorDrawable.setBounds(0, top, start, bottom)
                        mBackgroundColorDrawable.draw(c)
                    }

                    mDividerColorDrawable.setBounds(start, top, end, bottom)
                    mDividerColorDrawable.draw(c)
                    if (end > 0) {
                        mBackgroundColorDrawable.setBounds(end, top, parent.width, bottom)
                        mBackgroundColorDrawable.draw(c)
                    }
                } else {
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val left = child.right + params.rightMargin
                    val right = left + mHeight
                    if (start > 0) {
                        mBackgroundColorDrawable.setBounds(left, 0, right, end)
                        mBackgroundColorDrawable.draw(c)
                    }
                    mDividerColorDrawable.setBounds(left, start, right, end)
                    mDividerColorDrawable.draw(c)
                    if (end > 0) {
                        mBackgroundColorDrawable.setBounds(left, end, right, parent.height)
                        mBackgroundColorDrawable.draw(c)
                    }
                }
            }
        }
    }

    interface DividerFilter {
        fun skipDraw(position: Int): Boolean
    }

}