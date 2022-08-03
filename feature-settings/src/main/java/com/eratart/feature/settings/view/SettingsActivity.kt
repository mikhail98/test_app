package com.eratart.feature.settings.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.alert.AlertUtil
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.exception.BaseFailure
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.setDebounceTextChangedListener
import com.eratart.baseui.extensions.setOnTextChangedListener
import com.eratart.domain.model.domain.DroppUser
import com.eratart.feature.settings.R
import com.eratart.feature.settings.databinding.ActivitySettingsBinding
import com.eratart.feature.settings.di.SettingsModule
import com.eratart.feature.settings.failure.UnsavedChangesFailure
import com.eratart.feature.settings.viewmodel.SettingsViewModel
import com.eratart.photodialog.di.PhotoDialogModule
import com.eratart.photodialog.view.PhotoDialogBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity :
    BaseActivity<SettingsViewModel, ActivitySettingsBinding>(R.layout.activity_settings) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, SettingsActivity::class.java)
        }
    }

    override val koinModules = listOf(SettingsModule, PhotoDialogModule)
    override val viewModel: SettingsViewModel by viewModel()

    override val binding: ActivitySettingsBinding by viewBinding()
    private val tivFirstName by lazy { binding.tivFirstName }
    private val tivLastName by lazy { binding.tivLastName }
    private val tivNickname by lazy { binding.tivNickName }
    private val tivBio by lazy { binding.tivBio }
    private val ivAvatar by lazy { binding.ivAvatar }
    private val btnSelectAvatar by lazy { binding.btnSelectAvatar }

    private val sivAuth by lazy { binding.sivAuth }
    private val sivAboutApp by lazy { binding.sivAboutApp }
    private val sivPrivacyPolicy by lazy { binding.sivPrivacyPolicy }
    private val sivNotifications by lazy { binding.sivNotifications }

    private val btnBack by lazy { binding.btnBack }
    private val btnDone by lazy { binding.btnDone }
    private val btnLogout by lazy { binding.btnLogout }
    private val btnDeleteAccount by lazy { binding.btnDeleteAccount }

    private val photoDialogFragment by lazy {
        PhotoDialogBottomSheetFragment.Builder(this@SettingsActivity)
            .title(R.string.feature_settings_photo_dialog_title)
            .description(R.string.feature_settings_photo_description)
            .isGuideVisible(true)
            .photoResultListener(viewModel::handlePhoto)
    }

    override fun initView() {
        initClickListener()
        initTextWatchers()
    }

    private fun initTextWatchers() {
        tivFirstName.getEditText().setOnTextChangedListener {
            viewModel.updateFirstName(it)
        }
        tivLastName.getEditText().setOnTextChangedListener {
            viewModel.updateLastName(it)
        }
        tivBio.getEditText().setOnTextChangedListener {
            viewModel.updateBio(it)
        }
        tivNickname.getEditText().setOnTextChangedListener {
            viewModel.updateNickname(it)
        }
        tivNickname.getEditText().setDebounceTextChangedListener(1000L) {
            viewModel.checkUsernameAvailable(tivNickname.getText())
        }
    }

    private fun initClickListener() {
        btnSelectAvatar.setOnClickListener {
            photoDialogFragment.build().show(supportFragmentManager)
        }
        sivAuth.setOnClickListener {
            navigator.startAuthSettingsActivity(this)
        }
        sivAboutApp.setOnClickListener { navigator.startAboutAppActivity(this) }
        sivPrivacyPolicy.setOnClickListener {
            val privacyPolicyLink = getString(com.eratart.baseui.R.string.privacy_policy_link)
            navigator.startCustomTab(privacyPolicyLink)
        }
        sivNotifications.setOnClickListener {
            navigator.startNotificationsActivity(this)
        }
        btnBack.setOnClickListener { viewModel.closeScreen() }
        btnDone.setOnClickListener {
            if (tivNickname.getError().isEmpty()) {
                viewModel.saveUser()
            }
        }
        btnLogout.setOnClickListener { viewModel.logout() }
        btnDeleteAccount.setOnClickListener { viewModel.deleteAccount() }
    }

    override fun onBackPressed() {
        viewModel.closeScreen()
    }

    override fun renderFailure(failure: BaseFailure) {
        when (failure) {
            is UnsavedChangesFailure -> {
                AlertUtil.showWarningCancelAlert(
                    this,
                    R.string.feature_settings_unsaved_changes,
                    R.string.feature_settings_unsaved_changes_exit
                ) {
                    finish()
                }
            }
            else -> super.renderFailure(failure)
        }
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(logout, ::handleLogout)
            registerObserver(predefinedUser, ::handleUser)
            registerObserver(nicknameAvailable, ::handleNicknameAvailable)
            registerObserver(updatedAvatarUri, ::handleUpdatedUri)
        }
    }

    private fun handleUpdatedUri(uri: Uri) {
        ivAvatar.setImageURI(uri)
    }

    private fun handleNicknameAvailable(isAvailable: Boolean) {
        viewModel.updateNickname(tivNickname.getText())
        tivNickname.setErrorRes(R.string.feature_settings_setup_nickname_error.takeIf { !isAvailable })
    }

    private fun handleUser(user: DroppUser) {
        user.firstName?.apply {
            tivFirstName.setText(this)
        }
        user.lastName?.apply {
            tivLastName.setText(this)
        }
        user.photo?.apply {
            ivAvatar.loadImageWithGlide(this)
        }
        user.description?.apply {
            tivBio.setText(this)
        }
        user.username?.apply {
            tivNickname.setText(this)
        }
    }

    private fun handleLogout(isLogout: Boolean) {
        if (isLogout) {
            navigator.startSplashActivity(this)
        }
    }
}