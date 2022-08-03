package com.eratart.domain.model.other

import android.os.Parcelable
import com.eratart.domain.model.enums.AuthType
import kotlinx.parcelize.Parcelize

@Parcelize
class SignInData(
    val idToken: String?,
    val authType: AuthType
) : Parcelable