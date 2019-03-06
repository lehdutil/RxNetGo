package com.pingerx.baselib.widget.dialog

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.pingerx.baselib.R
import com.pingerx.baselib.utils.ViewUtils

/**
 * @author Pinger
 * @since 2019/1/3 18:01
 *
 * 确认对话框
 */
class MaterialConfirmDialog private constructor(context: Context) : MaterialDialog(context) {

    class Builder(context: Context,
                  private val msg: String,
                  private val title: String? = ViewUtils.getString(R.string.app_tips),
                  private val confirmText: String? = ViewUtils.getString(R.string.app_confirm),
                  private val listener: DialogInterface.OnClickListener? = null) : MaterialDialog.Builder(context) {

        override fun show(): AlertDialog {
            setTitle(title)
            setMessage(msg)
            setPositiveButton(confirmText, listener)
            setNegativeButton(ViewUtils.getString(R.string.app_cancel)) { dialog, _ -> dialog?.dismiss() }
            return super.show()
        }
    }
}