package com.eratart.domain.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Look(
    val title: String,
    val description: String,
    val photo: String
) : Parcelable