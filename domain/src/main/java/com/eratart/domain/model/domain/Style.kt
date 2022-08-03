package com.eratart.domain.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Style(
    val id: Long,
    val name: String,
): Parcelable