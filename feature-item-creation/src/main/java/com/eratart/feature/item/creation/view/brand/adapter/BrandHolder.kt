package com.eratart.feature.item.creation.view.brand.adapter

import androidx.core.view.isVisible
import com.eratart.feature.item.creation.databinding.ListItemBrandBinding
import com.eratart.feature.item.creation.view.brand.entity.BrandEntity

class BrandHolder(private val binding: ListItemBrandBinding, private val listener: IListener) :
    BaseBrandHolder<ListItemBrandBinding>(binding) {

    private val tvBrand by lazy { binding.tvBrand }
    private val ivBrandSelected by lazy { binding.ivBrandSelected }

    override fun bindItem(item: BrandEntity) {
        super.bindItem(item)
        tvBrand.text = item.brand?.name.orEmpty()
        ivBrandSelected.isVisible = item.isSelected
        itemView.setOnClickListener {
            listener.onBrandClick(item)
        }
    }

    interface IListener {
        fun onBrandClick(brandEntity: BrandEntity)
    }
}