package com.eratart.subfeature.items.view.adapter

import androidx.core.view.isVisible
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.setHeight
import com.eratart.domain.model.domain.Item
import com.eratart.subfeature.items.databinding.ListItemItemBinding

class ItemHolder(
    private val binding: ListItemItemBinding,
    private val itemCardHeight: Float,
    private val listener: IItemListener,
) :
    BaseRecyclerViewHolder<Item, ListItemItemBinding>(binding) {

    companion object{
        private const val MIN_MARK = 0.4f
    }

    private val ivImage by lazy { binding.ivImage }
    private val cvImage by lazy { binding.cvImage }
    private val tvTitle by lazy { binding.tvTitle }
    private val tvCategory by lazy { binding.tvCategory }
    private val tvMarkBad by lazy { binding.tvMarkBad }
    private val btnMore by lazy { binding.btnMore }

    override fun bindItem(item: Item) {
        super.bindItem(item)
        ivImage.loadImageWithGlide(item.photos.firstOrNull())
        cvImage.setHeight(itemCardHeight)
        tvCategory.text = item.category
        tvTitle.text = item.title
        tvMarkBad.isVisible = item.mark < MIN_MARK
        btnMore.setOnClickListener { listener.onMoreItemClick(item) }
    }

    interface IItemListener {
        fun onMoreItemClick(item: Item)
    }
}