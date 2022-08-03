package com.eratart.feature.onboarding.entity

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
class OnboardingModel(
    @StringRes
    val title: Int,
    @StringRes
    val description: Int,
    val image: String,
    val isSkippable: Boolean,
    val isSwipable: Boolean,
    @StringRes
    val buttonText: Int?,
) : Parcelable