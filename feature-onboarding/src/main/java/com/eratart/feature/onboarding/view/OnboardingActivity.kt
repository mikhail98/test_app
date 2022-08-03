package com.eratart.feature.onboarding.view

import android.app.Activity
import android.content.Intent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.setViewPageScroller
import com.eratart.baseui.view.viewpager.PageChangedListener2
import com.eratart.baseui.view.viewpager.ViewPageScroller
import com.eratart.core.constants.IntConstants
import com.eratart.feature.onboarding.R
import com.eratart.feature.onboarding.databinding.ActivityOnboardingBinding
import com.eratart.feature.onboarding.di.OnboardingModule
import com.eratart.feature.onboarding.entity.OnboardingModel
import com.eratart.feature.onboarding.entity.OnboardingModelsUtil
import com.eratart.feature.onboarding.view.viewpager.OnboardingViewPagerAdapter
import com.eratart.feature.onboarding.viewmodel.OnboardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingActivity :
    BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    companion object {
        private const val EXTRAS_SHOW_PROFILE_SETUP = "OnboardingActivity.EXTRAS_SHOW_PROFILE_SETUP"

        fun newInstance(activity: Activity, showProfileSetup: Boolean): Intent {
            return Intent(activity, OnboardingActivity::class.java).apply {
                putExtra(EXTRAS_SHOW_PROFILE_SETUP, showProfileSetup)
            }
        }
    }

    override val koinModules = listOf(OnboardingModule)
    override val viewModel: OnboardingViewModel by viewModel()


    private val showProfileSetup by lazy {
        intent.getBooleanExtra(EXTRAS_SHOW_PROFILE_SETUP, false)
    }
    private val models by lazy { OnboardingModelsUtil.getModelsList() }

    private val pagerAdapter by lazy { OnboardingViewPagerAdapter(this, models) }

    override val binding: ActivityOnboardingBinding by viewBinding()
    private val vpOnboarding by lazy { binding.vpOnboarding }
    private val btnNext by lazy { binding.btnNext }
    private val btnSkip by lazy { binding.btnSkip }
    private val tlDots by lazy { binding.tlDots }

    override fun initView() {
        changeStatusBarColor(com.eratart.baseui.R.color.background)
        vpOnboarding.apply {
            offscreenPageLimit = pagerAdapter.itemCount
            adapter = pagerAdapter
            tlDots.setViewPager2(this)
            setViewPageScroller(ViewPageScroller(context, AccelerateDecelerateInterpolator()))
            selectItem(IntConstants.ZERO)
            registerOnPageChangeCallback(PageChangedListener2 {
                handleModelSelected(models[it])
            })
        }
        btnNext.setOnClickListener {
            val currentItem = vpOnboarding.currentItem
            if (currentItem == models.size - IntConstants.ONE) {
                skipOnboarding()
            } else {
                selectItem(currentItem + IntConstants.ONE)
            }
        }
        btnSkip.setOnClickListener { skipOnboarding() }
    }

    private fun selectItem(pos: Int) {
        handleModelSelected(models[pos])
        vpOnboarding.setCurrentItem(pos, true)
    }

    private fun skipOnboarding() {
        viewModel.setOnboardingShown()
        if (showProfileSetup) {
            navigator.startProfileSetupActivity(this)
        } else {
            navigator.startMainActivity(this)
        }
        overrideAnimationAndClose()
    }

    private fun handleModelSelected(model: OnboardingModel) {
        vpOnboarding.isUserInputEnabled = model.isSwipable
        btnSkip.isVisible = model.isSkippable
    }

    override fun initViewModel() {

    }
}