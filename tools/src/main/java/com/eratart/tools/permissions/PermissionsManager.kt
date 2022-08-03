package com.eratart.tools.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.eratart.tools.permissions.IPermissionsManager.Companion.PERMISSION_REQUEST_CODE_CAMERA
import com.eratart.tools.permissions.IPermissionsManager.Companion.PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE

class PermissionsManager(private var activity: Activity) : IPermissionsManager {

    private fun isCameraPermissionEnabled() =
        ContextCompat.checkSelfPermission(
            activity, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun isReadExternalStoragePermissionEnabled() =
        ContextCompat.checkSelfPermission(
            activity, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun checkCameraPermissions(): Boolean {
        val isEnabled = isCameraPermissionEnabled()
        if (!isEnabled) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE_CAMERA)
        }
        return isEnabled
    }

    override fun checkReadExternalStoragePermission(): Boolean {
        val isEnabled = isReadExternalStoragePermissionEnabled()
        if (!isEnabled) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE
            )
        }
        return isEnabled
    }

    private fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        requestPermissions(activity, permissions, requestCode)
    }

    override fun isAllPermissionsEnabled(grantResults: IntArray) =
        grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
}