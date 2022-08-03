package com.eratart.feature.dialogs.list.view.recycler

import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.SearchItem

fun interface SearchItemActionClickListener {
    fun onClick(searchItem: SearchItem)
}