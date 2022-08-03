package com.eratart.domain.interactor.preferences

import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.domain.preferences.IOnboardingPreferences

class PreferencesInteractor(
    private val authPreferences: IAuthPreferences,
    private val onboardingPreferences: IOnboardingPreferences,
) : IPreferencesInteractor {

    override fun clear() {
        val onboardingShown = onboardingPreferences.isOnboardingShown()
        authPreferences.clear()
        onboardingPreferences.setOnboardingShown(onboardingShown)
    }
}