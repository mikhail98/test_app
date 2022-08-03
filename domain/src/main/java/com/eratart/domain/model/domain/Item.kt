package com.eratart.domain.model.domain

import android.os.Parcelable
import com.eratart.domain.model.enums.Season
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Long,
    val title: String,
    val category: String,
    val photos: List<String>,
    val mark: Float,
    val size: Size,
    val brand: String,
    val season: Season,
    val capsules: List<Capsule>,
    val looks: List<Look>,
) : Parcelable