package com.fungo.baselib.bugly

import android.content.Intent
import com.fungo.baselib.R
import com.fungo.baselib.utils.AppUtils
import com.fungo.baselib.utils.ToastUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.tencent.bugly.beta.Beta

/**
 * @author Pinger
 * @since 18-11-8 下午5:13
 *
 * APP内部升级助手,使用Bugly的升级
 */
object AppUpdater {


    private var isSilence: Boolean = true

    /**
     * @param isManual  用户手动点击检查，用户点击操作请传true
     * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
     */
    fun checkUpdate(isManual: Boolean, isSilence: Boolean) {
        this.isSilence = isSilence
        Beta.checkUpgrade(isManual, isSilence)
    }

    /**
     * 展示升级弹窗
     */
    fun showUpdateDialog() {
        // 启动升级Activity,在Activity中展示对话框
        val intent = Intent(AppUtils.getContext(), UpgradeDialogActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppUtils.getContext().startActivity(intent)
    }


    /**
     * 获取升级提示的标题
     * 前提是getUpgradeInfo()不为null
     * 只提供给UpgradeDialogActivity使用
     */
    fun getUpdateTitle(): String {
        return Beta.getUpgradeInfo().title
    }

    /**
     * 获取升级内容提示
     */
    fun getUpdateNewFeature(): String {
        return Beta.getUpgradeInfo().newFeature
    }


    /**
     * 开始下载App
     */
    fun startDownload() {
        Beta.startDownload()
    }

    fun showUpdateLeast() {
        if (!isSilence) {
            ToastUtils.showSuccess(ViewUtils.getString(AppUtils.getContext(), R.string.base_update_least))
        }
    }

}