package com.eratart.feature.item.creation.view.size.adapter

import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.domain.model.domain.Size
import com.eratart.feature.item.creation.databinding.ListItemSizeBinding

class SizeHolder(
    private val binding: ListItemSizeBinding,
) :
    BaseRecyclerViewHolder<Size, ListItemSizeBinding>(binding) {

    private val tvSize by lazy { binding.tvSize }

    override fun bindItem(item: Size) {
        super.bindItem(item)
        tvSize.text = item.size
    }
}