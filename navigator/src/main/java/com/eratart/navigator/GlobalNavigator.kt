package com.eratart.navigator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.eratart.baristashandbook.tools.customtabs.ICustomTabTool
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.Colors
import com.eratart.domain.model.domain.Item
import com.eratart.feature.about.app.view.AboutAppActivity
import com.eratart.feature.auth.view.AuthActivity
import com.eratart.feature.capsule.view.CapsuleActivity
import com.eratart.feature.dialog.view.DialogActivity
import com.eratart.feature.dialogs.list.view.DialogsListActivity
import com.eratart.feature.item.creation.view.ItemCreationActivity
import com.eratart.feature.item.view.ItemActivity
import com.eratart.feature.main.mainactivity.view.MainActivity
import com.eratart.feature.onboarding.view.OnboardingActivity
import com.eratart.feature.profile.setup.view.SetupProfileActivity
import com.eratart.feature.settings.view.SettingsActivity
import com.eratart.feature.splash.view.SplashActivity
import com.eratart.navigator.api.IGlobalNavigator
import com.eratart.navigator.api.ILauncherManager

class GlobalNavigator(
    private val customTabTool: ICustomTabTool, private val launcherManager: ILauncherManager,
) : IGlobalNavigator {

    private var colorPickerLauncher: ActivityResultLauncher<Colors?>? = null

    override fun startSplashActivity(activity: Activity) {
        activity.startActivity(SplashActivity.newInstance(activity))
    }

    override fun startAuthActivity(activity: Activity) {
        activity.startActivity(AuthActivity.newInstance(activity))
    }

    override fun startMainActivity(activity: Activity) {
        activity.startActivity(MainActivity.newInstance(activity))
    }

    override fun startOnboardingActivity(activity: Activity, showProfileSetup: Boolean) {
        activity.startActivity(OnboardingActivity.newInstance(activity, showProfileSetup))
    }

    override fun startProfileSetupActivity(activity: Activity) {
        activity.startActivity(SetupProfileActivity.newInstance(activity))
    }

    override fun startSettingsActivity(activity: Activity) {
        activity.startActivity(SettingsActivity.newInstance(activity))
    }

    override fun startAboutAppActivity(activity: Activity) {
        activity.startActivity(AboutAppActivity.newInstance(activity))
    }

    override fun startAuthSettingsActivity(activity: Activity) {
        Toast.makeText(activity, "startAuthSettingsActivity", Toast.LENGTH_SHORT).show()
    }

    override fun startNotificationsActivity(activity: Activity) {
        Toast.makeText(activity, "startNotificationsActivity", Toast.LENGTH_SHORT).show()
    }

    override fun startDialogActivity(activity: Activity) {
        activity.startActivity(DialogActivity.newInstance(activity))
    }

    override fun startDialogsListActivity(activity: Activity) {
        activity.startActivity(DialogsListActivity.newInstance(activity))
    }

    override fun startCapsuleActivity(activity: Activity, capsule: Capsule) {
        activity.startActivity(CapsuleActivity.newInstance(activity, capsule))
    }

    override fun startItemActivity(activity: Activity, item: Item, advert: Advert?) {
        activity.startActivity(ItemActivity.newInstance(activity, item, advert))
    }

    override fun startItemCreationActivity(activity: Activity) {
        activity.startActivity(ItemCreationActivity.newInstance(activity))
    }

    override fun startAdvertCreationActivity(activity: Activity, item: Item) {
        Toast.makeText(activity, "startAdvertCreationActivity", Toast.LENGTH_SHORT).show()
    }

    override fun initColorPickerLauncher(activity: Activity, listener: (Colors?) -> Unit) {
        if (activity is ComponentActivity) {
            colorPickerLauncher = launcherManager.getColorPickerLauncher(activity, listener)
        }
    }

    override fun startColorPickerActivity(colors: Colors?) {
        colorPickerLauncher?.launch(colors)
    }

    override fun startCustomTab(url: String) {
        customTabTool.openCustomTab(url)
    }

    override fun openEmailApp(activity: Activity, email: String, subject: String?, text: String?) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
            activity.startActivity(this)
        }
    }
}