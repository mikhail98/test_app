package com.eratart.domain.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val id: Long,
    val name: String,
): Parcelable