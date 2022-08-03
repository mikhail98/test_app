package com.eratart.feature.about.app.view

import android.app.Activity
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.core.BuildConfig
import com.eratart.feature.about.app.R
import com.eratart.feature.about.app.databinding.ActivityAboutAppBinding
import com.eratart.feature.about.app.di.AboutAppModule
import com.eratart.feature.about.app.viewmodel.AboutAppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutAppActivity :
    BaseActivity<AboutAppViewModel, ActivityAboutAppBinding>(R.layout.activity_about_app) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, AboutAppActivity::class.java)
        }
    }

    override val koinModules = listOf(AboutAppModule)
    override val viewModel: AboutAppViewModel by viewModel()

    override val binding: ActivityAboutAppBinding by viewBinding()
    private val btnBack by lazy { binding.btnBack }
    private val sivRate by lazy { binding.sivRate }
    private val sivConnect by lazy { binding.sivConnect }
    private val tvAppVersionName by lazy { binding.tvAppVersionName }
    private val clHeader by lazy { binding.clHeader }

    override fun initView() {
        btnBack.setOnClickListener { onBackPressed() }
        tvAppVersionName.text = BuildConfig.VERSION_NAME
        sivConnect.setOnClickListener {
            val email = getString(R.string.app_info_email)
            val subject = getString(R.string.app_info_email_subject)
            navigator.openEmailApp(this, email, subject, null)
        }
        sivRate.setOnClickListener {
            navigator.startCustomTab(BuildConfig.APP_URL_GOOGLE)
        }
    }

    override fun onLayoutReady() {
        addStatusBarMargin(clHeader)
    }

    override fun initViewModel() {
    }
}
