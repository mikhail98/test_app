package com.eratart.subfeature.chips.api

data class ChipItem<T>(
    val isSelected: Boolean,
    val text: String,
    val item: T,
)