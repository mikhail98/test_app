package com.eratart.feature.item.creation.view.brand.entity

import com.eratart.domain.model.domain.Brand

data class BrandEntity(
    val brand: Brand?,
    val isSelected: Boolean,
    val title: String?,
)