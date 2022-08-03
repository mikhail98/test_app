package com.eratart.feature.main.profile.view.capsules.view.adapter

import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.loadImageWithGlideBlur
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.visible
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Capsule
import com.eratart.feature.main.databinding.ListItemCapsuleBinding

class CapsuleHolder(
    private val binding: ListItemCapsuleBinding,
    private val itemCardHeight: Float,
    private val listener: ICapsuleListener,
) :
    BaseRecyclerViewHolder<Capsule, ListItemCapsuleBinding>(binding) {

    companion object {
        private const val BLUR_COUNT = 4
    }

    private val cvImages by lazy { binding.cvImages }
    private val ivImage1 by lazy { binding.ivImage1 }
    private val ivImage2 by lazy { binding.ivImage2 }
    private val ivImage3 by lazy { binding.ivImage3 }
    private val ivImage4 by lazy { binding.ivImage4 }

    private val tvTitle by lazy { binding.tvTitle }
    private val tvCategory by lazy { binding.tvCategory }
    private val tvMore by lazy { binding.tvMore }
    private val btnMore by lazy { binding.btnMore }

    override fun bindItem(item: Capsule) {
        super.bindItem(item)
        cvImages.setHeight(itemCardHeight)

        val items = item.items
        val images = items.map { it.photos.first() }
        when {
            items.size == BLUR_COUNT -> {
                loadImagesWithoutOne(images)
                ivImage4.loadImageWithGlide(images[3])
                tvMore.gone()
                tvMore.text = null
            }
            items.size > BLUR_COUNT -> {
                ivImage4.loadImageWithGlideBlur(images[3], 20, 1)
                loadImagesWithoutOne(images)
                tvMore.visible()
                tvMore.text = StringConstants.PLUS.plus(items.size - BLUR_COUNT)
            }
        }
        tvTitle.text = item.title
        tvCategory.text = "Вещей: ${items.size}, луков: ${item.looksCount}"
        btnMore.setOnClickListener { listener.onMoreCapsuleClick(item) }
    }

    private fun loadImagesWithoutOne(items: List<String>) {
        ivImage1.loadImageWithGlide(items[0])
        ivImage2.loadImageWithGlide(items[1])
        ivImage3.loadImageWithGlide(items[2])
    }

    interface ICapsuleListener {
        fun onMoreCapsuleClick(item: Capsule)
    }
}