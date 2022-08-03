package com.eratart.feature.profile.setup.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.setDebounceTextChangedListener
import com.eratart.baseui.extensions.setOnTextChangedListener
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.enums.Extravagance
import com.eratart.domain.model.enums.Gender
import com.eratart.feature.profile.setup.R
import com.eratart.feature.profile.setup.databinding.ActivitySetupProfileBinding
import com.eratart.feature.profile.setup.di.SetupProfileModule
import com.eratart.feature.profile.setup.view.bottomsheet.ClothesSexBottomSheetFragment
import com.eratart.feature.profile.setup.view.bottomsheet.ClothesTypeBottomSheetFragment
import com.eratart.feature.profile.setup.viewmodel.SetupProfileVewModel
import com.eratart.photodialog.di.PhotoDialogModule
import com.eratart.photodialog.view.PhotoDialogBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupProfileActivity :
    BaseActivity<SetupProfileVewModel, ActivitySetupProfileBinding>(R.layout.activity_setup_profile) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, SetupProfileActivity::class.java)
        }
    }

    override val koinModules = listOf(SetupProfileModule, PhotoDialogModule)
    override val viewModel: SetupProfileVewModel by viewModel()
    override val binding: ActivitySetupProfileBinding by viewBinding()
    private val sivClothesType by lazy { binding.sivClothesType }
    private val sivClothesSex by lazy { binding.sivClothesSex }
    private val btnSelectAvatar by lazy { binding.btnSelectAvatar }
    private val ivAvatar by lazy { binding.ivAvatar }
    private val btnNext by lazy { binding.btnNext }
    private val tivFirstName by lazy { binding.tivFirstName }
    private val tivLastName by lazy { binding.tivLastName }
    private val tivNickname by lazy { binding.tivNickname }
    private val tivBio by lazy { binding.tivBio }

    private val photoDialogBuilder by lazy {
        PhotoDialogBottomSheetFragment.Builder(this@SetupProfileActivity)
            .title(R.string.feature_profile_setup_photo_dialog_title)
            .description(R.string.feature_profile_setup_photo_description)
            .isGuideVisible(true)
            .photoResultListener(viewModel::handlePhoto)
    }

    override fun initView() {
        btnSelectAvatar.setOnClickListener {
            photoDialogBuilder.build().show(supportFragmentManager)
        }
        btnNext.setOnClickListener { viewModel.createUser() }

        sivClothesSex.setOnClickListener {
            val bottomSheetDialog = ClothesSexBottomSheetFragment.newInstance()
            bottomSheetDialog.show(supportFragmentManager)
        }
        sivClothesType.setOnClickListener {
            val bottomSheetDialog = ClothesTypeBottomSheetFragment.newInstance()
            bottomSheetDialog.show(supportFragmentManager)
        }
        tivFirstName.getEditText().setOnTextChangedListener {
            updateNextButtonState()
        }
        tivLastName.getEditText().setOnTextChangedListener {
            updateNextButtonState()
        }
        tivBio.getEditText().setOnTextChangedListener {
            updateNextButtonState()
        }
        tivNickname.getEditText().setDebounceTextChangedListener(1000L) {
            viewModel.checkUsernameAvailable(tivNickname.getText())
        }
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(sex, ::handleSex)
            registerObserver(extravagance, ::handleExtravagance)
            registerObserver(nicknameAvailable, ::handleNicknameAvailable)
            registerObserver(predefinedUser, ::handlePredefinedUser)
            registerObserver(userCreated, ::handleSuccessUserCreated)
            registerObserver(updatedAvatar, ::handleUpdatingAvatar)
            registerObserver(updatedAvatarUri, ::handleAvatarUri)
        }
    }

    private fun handleSuccessUserCreated(isCreated: Boolean) {
        if (isCreated) {
            navigator.startMainActivity(this)
        }
    }

    private fun handleNicknameAvailable(isAvailable: Boolean) {
        tivNickname.setErrorRes(R.string.feature_profile_setup_nickname_error.takeIf { !isAvailable })
        updateNextButtonState()
    }

    private fun handlePredefinedUser(data: DroppUser?) {
        data?.apply {
            firstName?.apply { tivFirstName.setText(this) }
            lastName?.apply { tivLastName.setText(this) }
            photo?.apply { ivAvatar.loadImageWithGlide(this) }
            gender?.apply { handleSex(this) }
            description?.apply { tivBio.setText(this) }
            username?.apply { tivNickname.setText(this) }
        }
    }

    private fun handleSex(sex: Gender) {
        val text = when (sex) {
            Gender.MAN -> R.string.feature_profile_setup_sex_man
            Gender.WOMAN -> R.string.feature_profile_setup_sex_woman
            Gender.UNISEX -> R.string.feature_profile_setup_sex_omen
        }
        sivClothesSex.setItem(text)
        updateNextButtonState()
    }

    private fun handleExtravagance(extravagance: Extravagance) {
        val text = when (extravagance) {
            Extravagance.LOW -> R.string.feature_profile_setup_type_low
            Extravagance.MEDIUM -> R.string.feature_profile_setup_type_medium
            Extravagance.HIGH -> R.string.feature_profile_setup_type_high
            Extravagance.ELTON_JOHN -> R.string.feature_profile_setup_type_elton
        }
        sivClothesType.setItem(text)
        updateNextButtonState()
    }

    private fun handleAvatarUri(uri: Uri) {
        ivAvatar.setImageURI(uri)
    }

    private fun handleUpdatingAvatar(url: String) {
        ivAvatar.loadImageWithGlide(url)
    }

    private fun updateNextButtonState() {
        var isActive = true

        val firstName = tivFirstName.getText()
        viewModel.updateFirstName(firstName)
        if (firstName.isEmpty()) {
            isActive = false
        }

        val lastName = tivLastName.getText()
        viewModel.updateLastName(lastName)
        if (lastName.isEmpty()) {
            isActive = false
        }

        val nickname = tivNickname.getText()
        viewModel.updateNickname(nickname)
        if (nickname.isEmpty()) {
            isActive = false
        }

        val bio = tivBio.getText()
        viewModel.updateBio(bio)
        if (bio.isEmpty()) {
            isActive = false
        }

        if (tivNickname.getError().isNotEmpty()) {
            isActive = false
        }
        if (sivClothesType.getItem().isEmpty()) {
            isActive = false
        }
        if (sivClothesSex.getItem().isEmpty()) {
            isActive = false
        }
        btnNext.setActive(isActive)
    }
}