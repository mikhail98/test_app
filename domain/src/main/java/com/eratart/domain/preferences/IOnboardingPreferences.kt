package com.eratart.domain.preferences

interface IOnboardingPreferences {

    companion object {
        const val ONBOARDING_SHOWN = "IOnboardingPreferences.ONBOARDING_SHOWN"
    }

    fun isOnboardingShown(): Boolean

    fun setOnboardingShown(isShown: Boolean)
}