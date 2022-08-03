package com.eratart.feature.item.creation.view.brand.adapter

import com.eratart.feature.item.creation.databinding.ListItemBrandHeaderBinding
import com.eratart.feature.item.creation.view.brand.entity.BrandEntity

class BrandHeaderHolder(private val binding: ListItemBrandHeaderBinding) :
    BaseBrandHolder<ListItemBrandHeaderBinding>(binding) {

    private val tvHeader by lazy { binding.tvHeader }

    override fun bindItem(item: BrandEntity) {
        super.bindItem(item)
        tvHeader.text = item.title.orEmpty()
    }
}