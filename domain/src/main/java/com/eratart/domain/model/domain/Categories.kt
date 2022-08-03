package com.eratart.domain.model.domain

import android.os.Parcelable
import com.eratart.domain.model.enums.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    val gender: Gender,
    val categories: List<Category>,
): Parcelable

@Parcelize
data class Category(
    val id: Long,
    val title: String,
    val key: String,
    val level: Int,
    val gender: Gender,
    val childCategories: List<Category>,
): Parcelable