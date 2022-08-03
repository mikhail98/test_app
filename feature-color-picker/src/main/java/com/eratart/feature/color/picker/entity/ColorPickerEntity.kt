package com.eratart.feature.color.picker.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorPickerEntity(
    val selectedColor: Int?,
    val colorPickerEntityType: ColorPickerEntityType?,
) : Parcelable

enum class ColorPickerEntityType {
    MAIN_COLOR, ADDITIONAL_COLOR_1, ADDITIONAL_COLOR_2
}