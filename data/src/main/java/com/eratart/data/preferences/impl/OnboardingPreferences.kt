package com.eratart.data.preferences.impl

import android.content.SharedPreferences
import com.eratart.data.preferences.BasePreferences
import com.eratart.domain.preferences.IOnboardingPreferences

class OnboardingPreferences(sharedPreferences: SharedPreferences) :
    BasePreferences(sharedPreferences), IOnboardingPreferences {

    override fun isOnboardingShown(): Boolean {
        return getObject(IOnboardingPreferences.ONBOARDING_SHOWN, false, Boolean::class.java)
    }

    override fun setOnboardingShown(isShown: Boolean) {
        saveObject(IOnboardingPreferences.ONBOARDING_SHOWN, isShown)
    }
}