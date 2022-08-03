package com.eratart.navigator.api

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.eratart.domain.model.domain.Colors

interface ILauncherManager {

    fun getColorPickerLauncher(
        activity: ComponentActivity, listener: (Colors?) -> Unit,
    ): ActivityResultLauncher<Colors?>
}