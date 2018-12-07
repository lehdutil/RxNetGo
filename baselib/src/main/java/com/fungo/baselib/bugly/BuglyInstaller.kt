package com.fungo.baselib.bugly

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.fungo.baselib.utils.AppUtils
import com.fungo.baselib.utils.DebugUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport

/**
 * @author Pinger
 * @since 18-11-8 下午2:35
 *
 * Bugly安装初始化管理
 *
 */
object BuglyInstaller {


    /**
     * 初始化Bugly
     */
    fun initBugly(context: Context, buglyId: String, tinkerDev: Boolean) {
        if (TextUtils.isEmpty(buglyId)) return

        // 异常检查上报控制策略
        val strategy = CrashReport.UserStrategy(context)
        // 上报主线程
        strategy.isUploadProcess = true
        // 设置同步延迟，修改成3秒
        strategy.appReportDelay = 3000
        strategy.appChannel = AppUtils.getChannel()

        // 升级初始化
        initUpgrade()

        // tinker热修复初始化
        Beta.enableHotfix = true                                    // 设置是否关闭热更新能力，默认为true
        Beta.canAutoDownloadPatch = true                            // 设置是否自动下载补丁
        Beta.canNotifyUserRestart = false                           // 设置是否提示用户重启
        Beta.canAutoPatch = true                                    // 设置是否自动合成补丁
        Beta.betaPatchListener = TinkerPatchListener()              // 补丁回调

        Bugly.setIsDevelopmentDevice(context, tinkerDev)  // 开发设备定义

        Bugly.setAppChannel(context, AppUtils.getChannel())         // 多渠道需求塞入
        Bugly.init(context, buglyId, DebugUtils.isDebugModel(), strategy)

    }


    /**
     * 安装Tinker
     */
    fun installTinker(buglyId: String) {
        if (TextUtils.isEmpty(buglyId)) return
        Beta.installTinker()
    }


    /**
     * 初始化升级
     */
    private fun initUpgrade() {
        Beta.autoInit = true  // 自动初始化
        Beta.autoCheckUpgrade = false  // 自动检测升级
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.enableNotification = true  // 设置是否显示消息通知
        Beta.autoDownloadOnWifi = true
        Beta.canShowApkInfo = false     // 设置是否显示弹窗中的apk信息
        Beta.showInterruptedStrategy = true

        // 注册监听,自动检测升级
        Beta.upgradeListener = UpgradeListener { _, upgradeInfo, _, _ ->
            if (upgradeInfo != null) {
                AppUpdater.showUpdateDialog()
            }
        }

        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgrading(p0: Boolean) {
            }

            override fun onUpgradeSuccess(p0: Boolean) {
            }

            override fun onDownloadCompleted(p0: Boolean) {
            }

            override fun onUpgradeNoVersion(p0: Boolean) {
                AppUpdater.showUpdateLeast()
            }

            override fun onUpgradeFailed(p0: Boolean) {
            }

        }
    }

}