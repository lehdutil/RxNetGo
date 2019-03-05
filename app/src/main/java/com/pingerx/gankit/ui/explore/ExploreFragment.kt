package com.pingerx.gankit.ui.explore

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.fungo.banner.BannerView
import com.fungo.banner.holder.BannerHolderCreator
import com.fungo.banner.holder.BaseBannerHolder
import com.fungo.baselib.base.fragment.BaseContentFragment
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.imagego.strategy.loadBlur
import com.fungo.imagego.strategy.loadImage
import com.pingerx.gankit.R
import com.pingerx.gankit.data.api.GankApi
import com.pingerx.gankit.ui.gank.GankDataBean
import com.pingerx.gankit.utils.LaunchUtils
import kotlinx.android.synthetic.main.fragment_explore.*

/**
 * @author Pinger
 * @since 18-12-7 上午11:03
 *
 * 发现福利页面，调用的是干货集中营福利数据
 * https://gank.io/api/data/福利/10/1
 */
class ExploreFragment : BaseContentFragment() {

    private val mBannerView by lazy {
        findView<BannerView<GankDataBean>>(R.id.bannerView)
    }

    private var mData: List<GankDataBean> = arrayListOf()

    override fun getContentResID(): Int = R.layout.fragment_explore

    override fun initEvent() {
        mBannerView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                loadBlur(mData[position].url)
            }
        })
    }


    override fun initData() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        showPageLoading()
        GankApi.getGankData(GankApi.GANK_TYPE_WELFARE, 0) {

            onSuccess {
                showPageContent()
                StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), false)
                mData = it.results
                loadBlur(mData[0].url)
                mBannerView.setPages(mData, object : BannerHolderCreator<GankDataBean, BannerHolder> {
                    override fun onCreateBannerHolder() = BannerHolder()
                })
            }

            onFailed { showPageError(it.getMsg()) }
        }
    }

    private fun loadBlur(url: String) {
        // 不需要加载图片得占位图
        loadBlur(url, ivBlurView, blurRadius = 8, blurSampling = 6, placeHolder = 0)
    }

    inner class BannerHolder : BaseBannerHolder<GankDataBean> {
        override fun getHolderResId() = R.layout.banner_holder_explore

        override fun onBindData(itemView: View, data: GankDataBean) {
            loadImage(data.url, itemView.findViewById(R.id.itemBanner))
        }

        override fun onPageClick(itemView: View, position: Int, data: GankDataBean) {
            LaunchUtils.startImagePreviewPage(itemView, data.url)
        }
    }

}