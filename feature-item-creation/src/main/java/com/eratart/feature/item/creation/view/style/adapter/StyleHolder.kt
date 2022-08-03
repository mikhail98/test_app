package com.eratart.feature.item.creation.view.style.adapter

import androidx.core.view.isVisible
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.feature.item.creation.databinding.ListItemStyleBinding
import com.eratart.feature.item.creation.view.style.entity.StyleEntity

class StyleHolder(
    private val binding: ListItemStyleBinding,
) :
    BaseRecyclerViewHolder<StyleEntity, ListItemStyleBinding>(binding) {

    private val tvStyleTitle by lazy { binding.tvStyleTitle }
    private val ivStyleSelected by lazy { binding.ivStyleSelected }

    override fun bindItem(item: StyleEntity) {
        super.bindItem(item)
        tvStyleTitle.text = item.style.name
        ivStyleSelected.isVisible = item.isSelected
    }
}