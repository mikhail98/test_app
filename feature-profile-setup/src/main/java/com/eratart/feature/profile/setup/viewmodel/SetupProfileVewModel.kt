package com.eratart.feature.profile.setup.viewmodel

import android.net.Uri
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.delayedLaunch
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.core.coroutines.onNextUi
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.enums.Extravagance
import com.eratart.domain.model.enums.Gender
import com.eratart.domain.model.enums.MediaFileType.PHOTO
import com.eratart.domain.model.enums.MediaType.AVATAR
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.tools.files.compress.IImageCompressor
import com.eratart.tools.files.uploader.IFileUploader
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SetupProfileVewModel(
    private val authPreferences: IAuthPreferences,
    private val usersInteractor: IUsersInteractor,
    private val fileUploader: IFileUploader,
    private val imageCompressor: IImageCompressor
) : BaseViewModel() {

    private var currentUser = authPreferences.getDroppUser()
    private var currentSex: Gender? = null
    private var currentExtravagance: Extravagance? = null
    private var newAvatarUri: Uri? = null

    private val _sex = MutableSharedFlow<Gender>()
    val sex = _sex.asSharedFlow()

    private val _extravagance = MutableSharedFlow<Extravagance>()
    val extravagance = _extravagance.asSharedFlow()

    private val _predefinedUser = MutableSharedFlow<DroppUser?>()
    val predefinedUser = _predefinedUser.asSharedFlow()

    private val _nicknameAvailable = MutableSharedFlow<Boolean>()
    val nicknameAvailable = _nicknameAvailable.asSharedFlow()

    private val _userCreated = MutableSharedFlow<Boolean>()
    val userCreated = _userCreated.asSharedFlow()

    private val _updatedAvatar = MutableSharedFlow<String>()
    val updatedAvatar = _updatedAvatar.asSharedFlow()

    private val _updatedAvatarUri = MutableSharedFlow<Uri>()
    val updatedAvatarUri = _updatedAvatarUri.asSharedFlow()

    override fun onCreate() {
        super.onCreate()

        fetchPredefinedUser()
    }

    private fun fetchPredefinedUser() {
        delayedLaunch {
            _predefinedUser.emit(currentUser)
        }
    }

    fun checkUsernameAvailable(username: String) {
        launchFlow {
            usersInteractor.usernameTaken(username)
                .catchUi { _nicknameAvailable.emit(true) }
                .onNextUi { _nicknameAvailable.emit(!it) }
        }
    }

    fun setSex(sex: Gender) {
        currentSex = sex
        launch { _sex.emit(sex) }
        currentUser = currentUser?.copy(gender = sex)
    }

    fun setExtravagance(extravagance: Extravagance) {
        currentExtravagance = extravagance
        launch { _extravagance.emit(extravagance) }
    }

    fun updateFirstName(firstName: String) {
        currentUser = currentUser?.copy(firstName = firstName)
    }

    fun updateLastName(lastName: String) {
        currentUser = currentUser?.copy(lastName = lastName)
    }

    fun updateNickname(username: String) {
        currentUser = currentUser?.copy(username = username)
    }

    fun updateBio(bio: String) {
        currentUser = currentUser?.copy(description = bio)
    }

    fun createUser() {
        currentUser?.apply {
            val userId = id ?: return
            launchFlow {
                getUploaderFlow(userId)
                    .flatMapConcat { url ->
                        usersInteractor.updateUser(
                            userId,
                            updateUserPhotoUrl(this, url)
                        )
                    }.subscribeWithError { user ->
                        authPreferences.saveDroppUser(user)
                        currentUser = user
                        _userCreated.emit(true)
                    }
            }
        }
    }

    fun handlePhoto(uri: Uri) {
        newAvatarUri = uri
        delayedLaunch { _updatedAvatarUri.emit(uri) }
    }

    @OptIn(FlowPreview::class)
    private fun getUploaderFlow(userId: Long): Flow<String?> {
        return newAvatarUri?.let { uri ->
            imageCompressor.compressByUri(uri)
                .flatMapConcat { compressedFileUri ->
                    fileUploader.uploadFileByUri(compressedFileUri, userId, AVATAR, PHOTO)
                }
        } ?: flow { emit(null) }
    }

    private fun updateUserPhotoUrl(droppUser: DroppUser, url: String?): DroppUser {
        return url?.let {
            droppUser.copy(photo = it)
        } ?: droppUser
    }
}