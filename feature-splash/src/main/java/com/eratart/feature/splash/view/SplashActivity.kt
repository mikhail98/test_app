package com.eratart.feature.splash.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.postDelayed
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.visible
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.core.constants.FloatConstants
import com.eratart.feature.splash.R
import com.eratart.feature.splash.databinding.ActivitySplashBinding
import com.eratart.feature.splash.di.SplashModule
import com.eratart.feature.splash.entity.SplashConstants.ANIM_DURATION
import com.eratart.feature.splash.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity :
    BaseActivity<SplashViewModel, ActivitySplashBinding>(R.layout.activity_splash) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, SplashActivity::class.java)
        }
    }

    override val koinModules = listOf(SplashModule)

    override val viewModel: SplashViewModel by viewModel()
    override val binding: ActivitySplashBinding by viewBinding()
    private val ivMainLogo by lazy { binding.ivMainLogo }
    private val ivLogo by lazy { binding.ivLogo }
    private val ivLines by lazy { binding.ivLines }
    private val ivBrand by lazy { binding.ivBrand }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        startAnimation()
        postDelayed(ANIM_DURATION * 2) {
            viewModel.fetchIsAuthorized()
        }
    }

    private fun startAnimation() {
        val deltaStart = FloatConstants.ZERO
        val startScale = FloatConstants.ONE
        val endScale = 1.5f
        val offsetHorizontal = 0.5f
        val offsetVertical = 0.5f

        val scaleAnim = ScaleAnimation(
            startScale, endScale, startScale, endScale,
            Animation.RELATIVE_TO_SELF, offsetHorizontal,
            Animation.RELATIVE_TO_SELF, offsetVertical
        )
        scaleAnim.fillAfter = false
        scaleAnim.duration = ANIM_DURATION
        scaleAnim.interpolator = BounceInterpolator()
        ivMainLogo.startAnimation(scaleAnim)

        postDelayed(ANIM_DURATION) {
            ivBrand.visibleWithAlpha(ANIM_DURATION)
            ivMainLogo.gone()
            ivLogo.visible()
            val linesAnim = TranslateAnimation(
                deltaStart, deltaStart, deltaStart, -getScreenHeight() / 2f
            )
            linesAnim.fillAfter = true
            linesAnim.duration = ANIM_DURATION
            linesAnim.interpolator = AccelerateDecelerateInterpolator()
            ivLines.startAnimation(linesAnim)
        }
    }

    override fun overrideAnimationAndClose() {
        overridePendingTransition(
            com.eratart.baseui.R.anim.fade_in_2,
            com.eratart.baseui.R.anim.fade_out
        )
        finish()
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(initData, ::handleIsAuthorized)
        }
    }

    private fun handleIsAuthorized(initData: Pair<Boolean, Boolean>) {
        if (initData.first) {
            if (initData.second) {
                navigator.startProfileSetupActivity(this)
            } else {
                navigator.startMainActivity(this)
            }
            finish()
        } else {
            navigator.startAuthActivity(this)
            overrideAnimationAndClose()
        }
    }
}