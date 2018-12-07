package com.fungo.baselib.utils

import android.text.TextUtils
import androidx.annotation.StringRes
import android.widget.Toast
import com.fungo.baselib.manager.ThreadManager
import com.fungo.baseuilib.theme.UiUtils
import es.dmoral.toasty.Toasty
import java.lang.AssertionError

/**
 * @author Pinger
 * @since 18-4-9 下午8:00
 *
 */
object ToastUtils {

    /**
     * 展示一个吐司，短时间的吐司
     */
    fun showToast(msg: String?) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 展示一个吐司
     */
    fun showToast(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), strId, Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 长时间展示的吐司
     */
    fun showLong(msg: String?) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_LONG).show()
        })
    }

    /**
     * 长时间展示的吐司
     */
    fun showLong(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), strId, Toast.LENGTH_LONG).show()
        })
    }


    /**
     * 只在开发环境才会展示的吐司，用于开发调试使用
     */
    fun testToast(msg: String?) {
        if (DebugUtils.isDevModel()) {
            ThreadManager.runOnUIThread(Runnable {
                Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT).show()
            })
        }
    }


    /**
     * 成功提示
     */
    fun showSuccess(success: String?){
        if (TextUtils.isEmpty(success)) {
            return
        }
        ThreadManager.runOnUIThread(Runnable {
            Toasty.success(AppUtils.getContext(), success!!, Toast.LENGTH_SHORT, true).show()
        })
    }

    /**
     * 错误提示
     */
    fun showError(error: String?){
        if (TextUtils.isEmpty(error)) {
            return
        }
        ThreadManager.runOnUIThread(Runnable {
            Toasty.error(AppUtils.getContext(), error!!, Toast.LENGTH_SHORT, true).show()
        })
    }


    fun showInfo(info: String?){
        if (TextUtils.isEmpty(info)) {
            return
        }
        ThreadManager.runOnUIThread(Runnable {
            Toasty.info(AppUtils.getContext(), info!!, Toast.LENGTH_SHORT, true).show()
        })
    }


    fun showWarning(warning: String?){
        if (TextUtils.isEmpty(warning)) {
            return
        }
        ThreadManager.runOnUIThread(Runnable {
            Toasty.warning(AppUtils.getContext(), warning!!, Toast.LENGTH_SHORT, true).show()
        })
    }

    fun showNormal(normal: String?){
        if (TextUtils.isEmpty(normal)) {
            return
        }
        ThreadManager.runOnUIThread(Runnable {
            Toasty.normal(AppUtils.getContext(), normal!!, Toast.LENGTH_SHORT).show()
        })
    }

}