package com.eratart.feature.onboarding.view.viewpager

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.feature.onboarding.entity.OnboardingModel
import com.eratart.feature.onboarding.view.fragments.OnboardingFragment

class OnboardingViewPagerAdapter(activity: FragmentActivity, models: List<OnboardingModel>) :
    BaseViewPager2Adapter(activity) {

    override val fragments by lazy {
        models.map { model -> OnboardingFragment.newInstance(model) }
    }
}