package com.eratart.feature.item.creation.view.categories.entity

import com.eratart.domain.model.domain.Category

data class CategoryEntity(
    val category: Category,
    val isSelected: Boolean,
)