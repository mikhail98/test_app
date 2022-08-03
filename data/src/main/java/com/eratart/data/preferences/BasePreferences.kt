package com.eratart.data.preferences

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.eratart.core.constants.StringConstants
import com.eratart.core.ext.printError
import com.google.gson.Gson

abstract class BasePreferences(private val sharedPreferences: SharedPreferences) {

    fun <T> getObject(propertyName: String, defaultValue: T, clazz: Class<T>): T {
        val string = sharedPreferences.getString(propertyName, StringConstants.EMPTY)
        return if (string.isNullOrEmpty()) {
            defaultValue
        } else {
            try {
                Gson().fromJson(string, clazz)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    fun <T> getObject(propertyName: String, clazz: Class<T>): T? {
        val string = sharedPreferences.getString(propertyName, StringConstants.EMPTY)
        return if (string.isNullOrEmpty()) {
            null
        } else {
            try {
                Gson().fromJson(string, clazz)
            } catch (e: Exception) {
                null
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    protected fun clearPrefs() {
        try {
            sharedPreferences.edit().clear().commit()
        } catch (e: Exception) {
            e.printError()
        }
    }

    @SuppressLint("ApplySharedPref")
    fun <T> saveObject(propertyName: String, obj: T) {
        sharedPreferences.edit().putString(propertyName, Gson().toJson(obj)).commit()
    }

}