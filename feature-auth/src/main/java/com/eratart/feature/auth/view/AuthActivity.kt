package com.eratart.feature.auth.view

import android.app.Activity
import android.content.Intent
import android.view.animation.TranslateAnimation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.goneWithAlpha
import com.eratart.baseui.extensions.postDelayed
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.setHtmlWithLinks
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.core.constants.FloatConstants
import com.eratart.domain.model.enums.AuthType
import com.eratart.feature.auth.R
import com.eratart.feature.auth.databinding.ActivityAuthBinding
import com.eratart.feature.auth.di.AuthModule
import com.eratart.feature.auth.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<AuthViewModel, ActivityAuthBinding>(R.layout.activity_auth) {

    companion object {
        private const val GOOGLE_REQUEST_CODE = 2
        private const val FIREBASE_REQUEST_CODE = 3
        private const val DURATION = 500L

        fun newInstance(context: Activity): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }

    override val koinModules = listOf(AuthModule)
    override val viewModel: AuthViewModel by viewModel()

    override val binding: ActivityAuthBinding by viewBinding()
    private val tvPolicy by lazy { binding.tvPolicy }
    private val ivLines by lazy { binding.ivLines }
    private val ivLogo by lazy { binding.ivLogo }
    private val ivBrand by lazy { binding.ivBrand }
    private val btnGoogle by lazy { binding.btnGoogle }
    private val btnFacebook by lazy { binding.btnFacebook }
    private val tvTitle by lazy { binding.tvTitle }
    private val tvDescription by lazy { binding.tvDescription }

    override fun initView() {
        tvPolicy.setHtmlWithLinks(R.string.feature_auth_policy)

        startAnimation()
        initClickListeners()
    }

    private fun initClickListeners() {
        btnGoogle.setOnClickListener {
            viewModel.login(this, GOOGLE_REQUEST_CODE, AuthType.GOOGLE)
        }
        btnFacebook.setOnClickListener {
            viewModel.login(this, FIREBASE_REQUEST_CODE, AuthType.FACEBOOK)
        }
    }

    private fun startAnimation() {
        ivBrand.goneWithAlpha()

        val deltaStart = FloatConstants.ZERO
        val duration = DURATION
        val linesAnim = TranslateAnimation(
            deltaStart, deltaStart, deltaStart, -getScreenHeight() / 4f
        )
        linesAnim.fillAfter = true
        linesAnim.duration = duration
        ivLines.startAnimation(linesAnim)
        ivLogo.startAnimation(linesAnim)

        postDelayed(duration) {
            tvPolicy.visibleWithAlpha()
            //btnFacebook.visibleWithAlpha()
            btnGoogle.visibleWithAlpha()
            tvTitle.visibleWithAlpha()
            tvDescription.visibleWithAlpha()
        }
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(authSuccessResult, ::handleAuthResult)
        }
    }

    private fun handleAuthResult(data: Pair<Boolean, Boolean>) {
        val needToShowProfileSetup = data.second
        if (data.first) {
            if (needToShowProfileSetup) {
                navigator.startProfileSetupActivity(this)
            } else {
                navigator.startMainActivity(this)
            }
        } else {
            navigator.startOnboardingActivity(this, needToShowProfileSetup)
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GOOGLE_REQUEST_CODE -> viewModel.authorizeGoogle(this, data)
            else -> viewModel.registerOnActivityResultFacebook(requestCode, resultCode, data)
        }
    }
}

