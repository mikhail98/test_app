package com.eratart.subfeature.chips.api

import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.subfeature.chips.api.databinding.ListItemChipsBinding

class ChipHolder<T : Any, VM : ChipItem<T>>(
    private val binding: ListItemChipsBinding,
) :
    BaseRecyclerViewHolder<VM, ListItemChipsBinding>(binding) {

    private val tvChips by lazy { binding.tvChips }

    override fun bindItem(item: VM) {
        super.bindItem(item)
        tvChips.text = item.text
        val bgRes = if (item.isSelected) {
            R.drawable.bg_chips_selected
        } else {
            R.drawable.bg_chips
        }
        itemView.setBackgroundResource(bgRes)
    }

}