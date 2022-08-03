package com.eratart.feature.item.creation.view.brand.adapter

import androidx.viewbinding.ViewBinding
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.feature.item.creation.view.brand.entity.BrandEntity

abstract class BaseBrandHolder<VB : ViewBinding>(binding: VB) :
    BaseRecyclerViewHolder<BrandEntity, VB>(binding)
