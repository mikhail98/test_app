package com.eratart.domain.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Capsule(
    val title: String,
    val looksCount: Int,
    val items: List<Item>,
    val mainPhoto: String,
    val description: String
) : Parcelable