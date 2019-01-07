package com.fungo.baselib.widget.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.fungo.baselib.R
import com.fungo.baselib.utils.ViewUtils

/**
 * @author Pinger
 * @since 2019/1/3 16:36
 *
 * 提示类型的对话框
 */
class MaterialTipsDialog private constructor(context: Context) : MaterialDialog(context) {

    class Builder(context: Context,
                  private val msg: String,
                  private val title: String? = ViewUtils.getString(R.string.app_tips),
                  private val confirmText: String? = ViewUtils.getString(R.string.app_confirm)) : MaterialDialog.Builder(context) {

        override fun show(): AlertDialog {
            setTitle(title)
            setMessage(msg)
            setPositiveButton(confirmText) { dialog, _ -> dialog?.dismiss() }
            return super.show()
        }
    }
}