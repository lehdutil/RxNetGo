package com.fungo.baselib.bugly

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.fungo.baselib.R
import com.fungo.baseuilib.activity.BaseActivity

/**
 * @author Pinger
 * @since 18-11-8 下午5:27
 *
 * 升级对话框页面
 * 初始化时弹出升级的Activity,确认后关闭Activity并且开始下载
 *
 */
class UpgradeDialogActivity(override val layoutResID: Int = R.layout.base_activity_upgrade) : BaseActivity() {


    override fun initView() {
        MaterialDialog(this)
            .title(text = AppUpdater.getUpdateTitle())
            .message(text = AppUpdater.getUpdateNewFeature())
            .cancelOnTouchOutside(false)
            .positiveButton(R.string.base_download)
            .positiveButton { AppUpdater.startDownload() }
            .onDismiss { finish() }
            .show()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun isSetAppTheme(): Boolean = false
}