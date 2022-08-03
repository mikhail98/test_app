package com.eratart.feature.dialogs.list.view.recycler

import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.DefaultDialog

fun interface OnDialogClickListener {
    fun onDialogClick(dialog: DefaultDialog)
}