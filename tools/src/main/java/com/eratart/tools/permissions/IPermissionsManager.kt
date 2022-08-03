package com.eratart.tools.permissions

interface IPermissionsManager {

    companion object {
        const val PERMISSION_REQUEST_CODE_CAMERA = 1112
        const val PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 1113
    }

    /**
     * Returns true if Manifest.permission.CAMERA is enabled
     */
    fun checkCameraPermissions(): Boolean


    /**
     * Returns true if Manifest.permission.READ_EXTERNAL_STORAGE is enabled
     */
    fun checkReadExternalStoragePermission(): Boolean

    /**
     * Returns true if all permissions at onPermissionResult is available
     */
    fun isAllPermissionsEnabled(grantResults: IntArray): Boolean

}