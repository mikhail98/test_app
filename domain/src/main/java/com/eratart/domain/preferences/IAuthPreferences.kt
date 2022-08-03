package com.eratart.domain.preferences

import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.other.SignInData

interface IAuthPreferences {

    companion object {
        const val USER_AUTHORIZATION = "IAuthPreferences.USER_AUTHORIZATION"
        const val DROPP_USER = "IAuthPreferences.DROPP_USER"
    }

    fun saveUserAuthorization(signInEntity: SignInData?)

    fun getUserAuthorization(): SignInData?

    fun getDroppUser(): DroppUser?

    fun saveDroppUser(user: DroppUser?)

    fun clear()
}