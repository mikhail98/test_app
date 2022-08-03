package com.eratart.navigator

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.eratart.domain.model.domain.Colors
import com.eratart.navigator.api.ILauncherManager
import com.eratart.navigator.contracts.ColorPickerContract

class LauncherManager : ILauncherManager {

    override fun getColorPickerLauncher(
        activity: ComponentActivity, listener: (Colors?) -> Unit,
    ): ActivityResultLauncher<Colors?> {
        return activity.registerForActivityResult(ColorPickerContract()) {
            listener.invoke(it)
        }
    }
}