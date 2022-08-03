package com.eratart.domain.model.domain

import android.os.Parcelable
import com.eratart.domain.model.enums.SizeType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    val size: String,
    val sizeType: SizeType,
) : Parcelable