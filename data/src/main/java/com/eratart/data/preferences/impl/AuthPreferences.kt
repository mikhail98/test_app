package com.eratart.data.preferences.impl

import android.content.SharedPreferences
import com.eratart.data.preferences.BasePreferences
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.other.SignInData
import com.eratart.domain.preferences.IAuthPreferences

class AuthPreferences(private val sharedPreferences: SharedPreferences) :
    BasePreferences(sharedPreferences), IAuthPreferences {

    override fun saveUserAuthorization(signInEntity: SignInData?) {
        saveObject(IAuthPreferences.USER_AUTHORIZATION, signInEntity)
    }

    override fun getUserAuthorization(): SignInData? {
        return getObject(IAuthPreferences.USER_AUTHORIZATION, SignInData::class.java)
    }

    override fun getDroppUser(): DroppUser? {
        return getObject(IAuthPreferences.DROPP_USER, DroppUser::class.java)
    }

    override fun saveDroppUser(user: DroppUser?) {
        saveObject(IAuthPreferences.DROPP_USER, user)
    }

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}