package com.eratart.baseui.extensions

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.registerObserver(flow: Flow<T>, action: (T) -> Unit) {
    lifecycleScope.launch {
        flow
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { action.invoke(it) }
    }
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T : Any, L : LiveData<T>> LifecycleOwner.removeObservers(liveData: L) =
    liveData.removeObservers(this)

fun <T : Any, L : LiveData<T?>> LifecycleOwner.observeNullable(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun Context.checkServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager?
    for (service in manager?.getRunningServices(Int.MAX_VALUE) ?: listOf()) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}