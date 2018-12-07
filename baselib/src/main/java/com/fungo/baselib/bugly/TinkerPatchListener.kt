package com.fungo.baselib.bugly

import com.fungo.baselib.utils.LogUtils
import com.tencent.bugly.beta.interfaces.BetaPatchListener

/**
 * @author Pinger
 * @since 18-11-8 下午5:20
 *
 * Bugly补丁的回调
 */
class TinkerPatchListener : BetaPatchListener {

    override fun onApplySuccess(p0: String?) {
        LogUtils.e("应用补丁成功 ---> $p0")
    }

    override fun onPatchReceived(p0: String?) {
        LogUtils.e("接收补丁 ---> $p0")
    }

    override fun onApplyFailure(p0: String?) {
        LogUtils.e("应用补丁失败 ---> $p0")
    }

    override fun onDownloadReceived(p0: Long, p1: Long) {
        LogUtils.e("--- 补丁接收完毕 ---")
    }

    override fun onDownloadSuccess(p0: String?) {
        LogUtils.e("接收补丁成功 ---> $p0")
    }

    override fun onDownloadFailure(p0: String?) {
        LogUtils.e("接收补丁失败 ---> $p0")
    }

    override fun onPatchRollback() {
        LogUtils.e("--- 补丁回滚 ---")
    }
}