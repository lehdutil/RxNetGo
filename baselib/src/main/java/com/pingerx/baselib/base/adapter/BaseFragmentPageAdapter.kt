package com.pingerx.baselib.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.pingerx.baselib.base.fragment.BaseFragment

/**
 * @author Pinger
 * @since 2018/10/31 21:27
 *
 * 给ViewPager创建Fragment使用
 *
 */
class BaseFragmentPageAdapter : FragmentStatePagerAdapter {

    private var mFragments = listOf<BaseFragment>()
    private var mTitles = listOf<String>()

    constructor(manager: FragmentManager) : super(manager)

    constructor(manager: FragmentManager, fragments: List<BaseFragment>) : super(manager) {
        this.mFragments = fragments
    }

    constructor(manager: FragmentManager, fragments: Array<BaseFragment>) : super(manager) {
        this.mFragments = fragments.toList()
    }

    constructor(manager: FragmentManager, fragments: List<BaseFragment>, titles: List<String>) : super(manager) {
        this.mFragments = fragments
        this.mTitles = titles
    }

    constructor(manager: FragmentManager, fragments: Array<BaseFragment>, titles: Array<String>) : super(manager) {
        this.mFragments = fragments.toList()
        this.mTitles = titles.toList()
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles.isNotEmpty()) {
            mTitles[position]
        } else {
            super.getPageTitle(position)
        }
    }

    /**
     * 更新数据
     */
    fun setData(fragments: ArrayList<BaseFragment>, titles: ArrayList<String>) {
        mFragments = fragments
        mTitles = titles
        notifyDataSetChanged()
    }
}