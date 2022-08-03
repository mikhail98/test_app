package com.eratart.navigator.api

import android.app.Activity
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.Colors
import com.eratart.domain.model.domain.Item

interface IGlobalNavigator {

    fun startSplashActivity(activity: Activity)

    fun startAuthActivity(activity: Activity)

    fun startMainActivity(activity: Activity)

    fun startOnboardingActivity(activity: Activity, showProfileSetup: Boolean)

    fun startProfileSetupActivity(activity: Activity)

    fun startSettingsActivity(activity: Activity)

    fun startAboutAppActivity(activity: Activity)

    fun startNotificationsActivity(activity: Activity)

    fun startAuthSettingsActivity(activity: Activity)

    fun startDialogsListActivity(activity: Activity)

    fun startDialogActivity(activity: Activity)

    fun startCapsuleActivity(activity: Activity, capsule: Capsule)

    fun startItemActivity(activity: Activity, item: Item, advert: Advert?)

    fun startItemCreationActivity(activity: Activity)

    fun initColorPickerLauncher(activity: Activity, listener: (Colors?) -> Unit)

    fun startColorPickerActivity(colors: Colors?)

    fun startAdvertCreationActivity(activity: Activity, item: Item)

    fun startCustomTab(url: String)

    fun openEmailApp(activity: Activity, email: String, subject: String?, text: String?)
}