package com.eratart.subfeature.looks.view.adapter

import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.setWidth
import com.eratart.domain.model.domain.Look
import com.eratart.subfeature.looks.databinding.ListItemLookBinding

class LookHolder(
    private val binding: ListItemLookBinding,
    private val itemCardHeight: Float,
    private val itemCardWidth: Float,
    private val listener: IItemListener,
) :
    BaseRecyclerViewHolder<Look, ListItemLookBinding>(binding) {

    private val ivImage by lazy { binding.ivImage }
    private val cvImage by lazy { binding.cvImage }
    private val tvTitle by lazy { binding.tvTitle }
    private val tvCategory by lazy { binding.tvCategory }
    private val btnMore by lazy { binding.btnMore }

    override fun bindItem(item: Look) {
        super.bindItem(item)
        ivImage.loadImageWithGlide(item.photo)
        cvImage.setHeight(itemCardHeight)
        cvImage.setWidth(itemCardWidth)
        tvCategory.text = item.description
        tvTitle.text = item.title
        btnMore.setOnClickListener { listener.onMoreLookClick(item) }
    }

    interface IItemListener {
        fun onMoreLookClick(item: Look)
    }
}