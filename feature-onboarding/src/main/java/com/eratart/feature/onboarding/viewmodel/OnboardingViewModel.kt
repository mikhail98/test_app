package com.eratart.feature.onboarding.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.domain.preferences.IOnboardingPreferences

class OnboardingViewModel(
    private val onboardingPreferences: IOnboardingPreferences
) : BaseViewModel() {

    fun setOnboardingShown() {
        onboardingPreferences.setOnboardingShown(true)
    }

}