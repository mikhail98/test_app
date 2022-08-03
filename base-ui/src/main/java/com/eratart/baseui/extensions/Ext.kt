package com.eratart.baseui.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.CombinedVibration
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.eratart.core.constants.IntConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Context.addToClipboard(text: String, toastText: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Eyezon business", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
}

fun Context.addToClipboard(text: String, @StringRes stringRes: Int) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Eyezon business", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, stringRes, Toast.LENGTH_LONG).show()
}

fun Context.triggerRebirth() {
    val packageManager = packageManager
    packageManager.getLaunchIntentForPackage(packageName)?.apply {
        val mainIntent = Intent.makeRestartActivityTask(component)
        startActivity(mainIntent)
        Runtime.getRuntime().exit(IntConstants.ZERO)
    }
}

fun ImageView.changeDrawableColor(color: Int) {
    DrawableCompat.setTint(DrawableCompat.wrap(this.drawable), color)
}

fun EditText.setOnImeActionSearchClickListener(listener: (String) -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            listener.invoke(this.text.toString())
            true
        } else {
            false
        }
    }
}

fun Context?.vibrate(duration: Long = 5L) {
    this?.apply {
        val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager?
            val vibration = CombinedVibration.createParallel(effect)
            vibrator?.vibrate(vibration)
        } else {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator?
            vibrator?.vibrate(effect)
        }
    }
}

fun Context.createView(resId: Int) = LayoutInflater.from(this).inflate(resId, null)!!

fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun Context.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > IntConstants.ZERO) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        IntConstants.ZERO
    }
}

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > IntConstants.ZERO) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        IntConstants.ZERO
    }
}

fun Context.getScreenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    getWindowManager().defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Context.getScreenHeight(): Int {
    val displayMetrics = DisplayMetrics()
    getWindowManager().defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Activity.hideSystemUI(mainContainer: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, mainContainer).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.showSystemUI(mainContainer: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars())
}

fun Activity.getStatusBarHeight(): Int {
    val rectangle = Rect()

    window.decorView.getWindowVisibleDisplayFrame(rectangle)
    return rectangle.top
}

fun Activity.addStatusBarMargin(view: View) {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    params.topMargin = params.topMargin + getStatusBarHeight()
    view.layoutParams = params
}

fun postDelayed(duration: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        action.invoke()
    }, duration)
}

fun CoroutineScope.delayedLaunch(durationInMills: Long = 50, action: suspend () -> Unit) {
    postDelayed(durationInMills) {
        launch { action() }
    }
}