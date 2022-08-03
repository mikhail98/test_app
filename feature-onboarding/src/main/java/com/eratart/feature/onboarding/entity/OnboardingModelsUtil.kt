package com.eratart.feature.onboarding.entity

import com.eratart.feature.onboarding.R

object OnboardingModelsUtil {

    private val model1 by lazy {
        OnboardingModel(
            R.string.feature_onboarding_fragment_1_title,
            R.string.feature_onboarding_fragment_1_description,
           "https://blog.luxxy.com/wp-content/uploads/2021/02/original-1024x1022.jpg",
            isSkippable = false,
            isSwipable = true,
            R.string.feature_onboarding_next_button_text
        )
    }

    private val model2 by lazy {
        OnboardingModel(
            R.string.feature_onboarding_fragment_2_title,
            R.string.feature_onboarding_fragment_2_description,
            "https://fainaidea.com/wp-content/uploads/2019/08/2-19.jpg",
            isSkippable = true,
            isSwipable = true,
            R.string.feature_onboarding_next_button_text
        )
    }

    private val model3 by lazy {
        OnboardingModel(
            R.string.feature_onboarding_fragment_3_title,
            R.string.feature_onboarding_fragment_3_description,
            "https://85.img.avito.st/640x480/13758699085.jpg",
            isSkippable = true,
            isSwipable = true,
            R.string.feature_onboarding_next_button_text
        )
    }

    fun getModelsList() = listOf(model1, model2, model3)

}