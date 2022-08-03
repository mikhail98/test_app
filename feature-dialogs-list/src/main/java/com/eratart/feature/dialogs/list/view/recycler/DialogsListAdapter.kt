package com.eratart.feature.dialogs.list.view.recycler

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.feature.dialogs.list.R
import com.eratart.feature.dialogs.list.databinding.ItemDialogBinding
import com.eratart.feature.dialogs.list.databinding.ItemDialogSearchBinding
import com.eratart.feature.dialogs.list.databinding.ItemNotFoundBinding
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.*
import com.eratart.feature.dialogs.list.view.recycler.holders.DialogHolder
import com.eratart.feature.dialogs.list.view.recycler.holders.DialogsListHolder
import com.eratart.feature.dialogs.list.view.recycler.holders.NotFoundHolder
import com.eratart.feature.dialogs.list.view.recycler.holders.SearchItemHolder

class DialogsListAdapter(
    private val onSearchItemActionClickListener: SearchItemActionClickListener,
    private val onDialogClickListener: OnDialogClickListener,
    private val viewModels: MutableList<DialogsListScreenItems> = mutableListOf()
) : BaseRecyclerAdapter<DialogsListScreenItems>(viewModels) {

    companion object {
        private const val DIALOG = 1
        private const val SEARCH = 2
        private const val NOT_FOUND = 3
    }

    override fun getBindingViewHolder(
        viewType: Int,
        parent: ViewGroup
    ): DialogsListHolder<*> {
        return when (viewType) {
            DIALOG -> DialogHolder(
                binding = ItemDialogBinding.inflate(getInflater(parent), parent, false),
                clickListener = onDialogClickListener
            )

            SEARCH -> SearchItemHolder(
                binding = ItemDialogSearchBinding.inflate(
                    getInflater(parent),
                    parent,
                    false
                ),
                clickListener = onSearchItemActionClickListener
            )
            NOT_FOUND -> NotFoundHolder(
                view = ItemNotFoundBinding.inflate(getInflater(parent), parent, false)
            )
            else -> error("${javaClass.name} $viewType is unsupported")
        }
    }

    override fun getItemViewType(position: Int) = when(viewModels[position]) {
        is DefaultDialog -> DIALOG
        is SearchItem -> SEARCH
        is NotFound -> NOT_FOUND
    }
}