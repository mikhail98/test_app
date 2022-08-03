package com.eratart.feature.settings.viewmodel

import android.net.Uri
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.delayedLaunch
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.core.coroutines.onNextUi
import com.eratart.domain.interactor.preferences.IPreferencesInteractor
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.enums.MediaFileType.PHOTO
import com.eratart.domain.model.enums.MediaType.AVATAR
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.feature.settings.failure.UnsavedChangesFailure
import com.eratart.tools.auth.IAuthTool
import com.eratart.tools.files.compress.IImageCompressor
import com.eratart.tools.files.uploader.IFileUploader
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authPreferences: IAuthPreferences,
    private val preferencesInteractor: IPreferencesInteractor,
    private val usersInteractor: IUsersInteractor,
    private val authTool: IAuthTool,
    private val fileUploader: IFileUploader,
    private val imageCompressor: IImageCompressor
) : BaseViewModel() {

    private var initUser = authPreferences.getDroppUser()
    private var currentUser = authPreferences.getDroppUser()
    private var newAvatarUri: Uri? = null

    private val _nicknameAvailable = MutableSharedFlow<Boolean>()
    val nicknameAvailable = _nicknameAvailable.asSharedFlow()

    private val _predefinedUser = MutableSharedFlow<DroppUser>()
    val predefinedUser = _predefinedUser.asSharedFlow()

    private val _logout = MutableSharedFlow<Boolean>()
    val logout = _logout.asSharedFlow()

    private val _updatedAvatarUri = MutableSharedFlow<Uri>()
    val updatedAvatarUri = _updatedAvatarUri.asSharedFlow()

    override fun onCreate() {
        super.onCreate()

        fetchPredefinedUser()
    }

    private fun fetchPredefinedUser() {
        postDelayed(50) {
            if (initUser == null) {
                logout()
            } else {
                launch {
                    initUser?.apply { _predefinedUser.emit(this) }
                }
            }
        }
    }

    fun checkUsernameAvailable(username: String) {
        if (username == initUser?.username) {
            launch { _nicknameAvailable.emit(true) }
            return
        }
        launchFlow {
            usersInteractor.usernameTaken(username)
                .catchUi { _nicknameAvailable.emit(true) }
                .onNextUi { _nicknameAvailable.emit(!it) }
        }
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

    fun saveUser() {
        if (initUser == currentUser) {
            closeScreen()
        } else {
            currentUser?.apply {
                val userId = id ?: return
                launchFlow {
                    getUploaderFlow(userId)
                        .applyLoader()
                        .flatMapConcat { url ->
                            usersInteractor.updateUser(
                                userId,
                                updateUserPhotoUrl(this, url)
                            )
                        }.subscribeWithError {
                            authPreferences.saveDroppUser(it)
                            initUser = it
                            currentUser = it
                            closeScreen()
                        }
                }
            }
        }
    }

    fun deleteAccount() {
    }

    fun closeScreen() {
        if (initUser != currentUser) {
            handleFailure(UnsavedChangesFailure())
        } else {
            closeActivity()
        }
    }

    fun logout() {
        preferencesInteractor.clear()
        authTool.logout()
        launch { _logout.emit(true) }
    }

    fun handlePhoto(uri: Uri) {
        newAvatarUri = uri
        currentUser = currentUser?.copy(photo = uri.toString())
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