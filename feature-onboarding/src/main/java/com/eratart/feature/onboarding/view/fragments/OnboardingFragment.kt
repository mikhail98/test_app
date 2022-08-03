package com.eratart.feature.onboarding.view.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.withArgs
import com.eratart.feature.onboarding.R
import com.eratart.feature.onboarding.databinding.FragmentOnboardingBinding
import com.eratart.feature.onboarding.entity.OnboardingModel
import com.eratart.feature.onboarding.viewmodel.OnboardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingBinding>(R.layout.fragment_onboarding) {

    companion object {
        private const val ARGS_ONBOARDING_MODEL = "OnboardingFragment.ARGS_ONBOARDING_MODEL"

        fun newInstance(onboardingModel: OnboardingModel) = OnboardingFragment().withArgs {
            putParcelable(ARGS_ONBOARDING_MODEL, onboardingModel)
        }
    }

    private val margin by lazy { 32f.dpToPx() }

    override val binding: FragmentOnboardingBinding by viewBinding()
    override val viewModel: OnboardingViewModel by viewModel()

    private val onboardingModel by lazyArgument<OnboardingModel>(ARGS_ONBOARDING_MODEL)

    override fun initFragment() {
    }

    override fun initView() {
        onboardingModel?.apply {
            binding.apply {
                ivOnboardingImage.loadImageWithGlide(image)
                ivOnboardingImage.setHeight(requireActivity().getScreenWidth() - margin)
                tvOnboardingTitle.setText(title)
                tvOnboardingDescription.setText(description)
            }
        }
    }
}