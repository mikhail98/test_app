package com.eratart.domain.model.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Colors(
    val imageUri: Uri?,
    val mainColor: Int?,
    val additionalColors: List<Int>?,
): Parcelable