package com.eratart.subfeature.items.view.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.domain.model.domain.Item
import com.eratart.subfeature.items.databinding.ListItemItemBinding

class ItemsAdapter(
    viewModels: MutableList<Item>,
    private val itemCardHeight: Float,
    private val listener: IListener
) :
    BaseRecyclerAdapter<Item>(viewModels) {


    override fun getBindingViewHolder(
        viewType: Int, parent: ViewGroup,
    ): ItemHolder {
        val view = ListItemItemBinding.inflate(getInflater(parent), parent, false)
        return ItemHolder(view, itemCardHeight, listener)
    }

    interface IListener : ItemHolder.IItemListener

}