package com.fungo.baselib.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.fungo.baseuilib.activity.BaseSwipeBackActivity
import com.fungo.baseuilib.fragment.BaseFragment

/**
 * @author Pinger
 * @since 18-7-25 下午2:18
 *
 * 跳转Web页面的中转，只用来分发H5Fragment页面
 */

class WebActivity : BaseSwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
    }

    override fun getContentFragment(): BaseFragment {
        return WebFragment()
    }

    override fun isShowToolBar(): Boolean = false

    /**
     * 跳转暂时使用传统方法
     */
    companion object {
        fun start(context: Context?, url: String?, title: String? = null, webBack: Boolean = true) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WebConstant.KEY_WEB_URL, url)
            intent.putExtra(WebConstant.KEY_WEB_TITLE, title)
            intent.putExtra(WebConstant.KEY_WEB_BACK, webBack)
            intent.putExtra(WebConstant.KEY_WEB_SWIPE_BACK, false)
            context?.startActivity(intent)
        }
    }

}