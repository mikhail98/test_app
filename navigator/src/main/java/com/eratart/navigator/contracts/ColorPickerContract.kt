package com.eratart.navigator.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.eratart.domain.model.domain.Colors
import com.eratart.feature.color.picker.view.ColorPickerActivity

class ColorPickerContract : ActivityResultContract<Colors?, Colors?>() {

    private var input: Colors? = null

    override fun createIntent(context: Context, input: Colors?): Intent {
        this.input = input
        return ColorPickerActivity.newInstance(context as Activity, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Colors? {
        return ColorPickerActivity.parseResult(resultCode, intent, input)
    }
}