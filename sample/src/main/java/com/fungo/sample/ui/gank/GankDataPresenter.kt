package com.fungo.sample.ui.gank

import com.fungo.baselib.utils.AppUtils
import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.baseuilib.utils.ViewUtils
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.GankApi
import java.util.*

/**
 * @author Pinger
 * @since 18-12-7 上午11:16
 */
class GankDataPresenter(private val gankView: BaseRecyclerContract.View, private val gankType: String) : BaseRecyclerContract.Presenter {


    override fun loadData(page: Int) {
        GankApi.getGankData(gankType, page, object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                if (!data.error) {
                    if (gankType == GankApi.GANK_TYPE_WELFARE) {
                        gankView.showContent(generateRandomHeight(data.results))
                    } else {
                        gankView.showContent(data.results)
                    }
                } else {
                    gankView.showPageEmpty()
                }
            }

            override fun onError(exception: ApiException) {
                gankView.showPageError(exception.getMsg())
            }
        })
    }

    /**
     * 获取随机高度，范围为宽度的1-5/3倍
     */
    private fun generateRandomHeight(data: List<GankDataBean>): List<GankDataBean> {
        // 图片的宽度为屏幕宽度半
        val width = ViewUtils.getScreenWidth(AppUtils.getContext()) / 2
        val random = Random()
        data.forEach {
            it.height = (1f * width * (random.nextInt(5) % 3 + 3) / 3).toInt()
        }
        return data
    }
}