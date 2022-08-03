package com.eratart.feature.dialogs.list.view.recycler.holders

import androidx.viewbinding.ViewBinding
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems

abstract class DialogsListHolder<VB : ViewBinding>(protected val binding: VB) :
    BaseRecyclerViewHolder<DialogsListScreenItems, VB>(binding)