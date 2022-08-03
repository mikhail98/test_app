package com.eratart.data.preferences

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.eratart.core.BuildConfig
import com.eratart.core.constants.StringConstants

object Preferences {
    const val PREFERENCES_NAME = BuildConfig.APP_ID.plus(StringConstants.DOT).plus("preferences")

    val keyEncryptor = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
    val valueEncryptor = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM

    fun createAES256GCMKeyGenParameterSpec() = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .build()
}