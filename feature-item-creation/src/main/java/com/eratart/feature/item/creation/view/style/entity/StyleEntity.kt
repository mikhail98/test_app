package com.eratart.feature.item.creation.view.style.entity

import com.eratart.domain.model.domain.Style

data class StyleEntity(
    val style: Style,
    val isSelected: Boolean,
)