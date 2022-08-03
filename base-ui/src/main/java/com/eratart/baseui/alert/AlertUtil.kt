package com.eratart.baseui.alert

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.eratart.baseui.R

object AlertUtil {

    fun showOkAlert(context: Context, @StringRes titleRes: Int, @StringRes descriptionRes: Int) {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(titleRes)
            .setMessage(descriptionRes)
            .setPositiveButton(R.string.action_ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun showCancelAlert(
        context: Context,
        @StringRes titleRes: Int,
        @StringRes actionRes: Int,
        listener: () -> Unit,
    ) {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(titleRes)
            .setPositiveButton(actionRes) { dialog, _ ->
                listener.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.action_cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun showWarningCancelAlert(
        context: Context,
        @StringRes messageRes: Int,
        @StringRes actionRes: Int,
        listener: () -> Unit,
    ) {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(R.string.alert_warning)
            .setMessage(messageRes)
            .setPositiveButton(actionRes) { dialog, _ ->
                listener.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.action_cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}