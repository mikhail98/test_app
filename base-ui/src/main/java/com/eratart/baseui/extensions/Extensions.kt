package com.eratart.baseui.extensions

import android.app.job.JobScheduler
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.eratart.core.constants.IntConstants
import com.eratart.core.coroutines.onNext
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn

fun Chronometer.getTime() = SystemClock.elapsedRealtime() - this.base

fun <T> MutableList<T>.replaceAllWith(collection: Collection<T>) {
    this.clear()
    this.addAll(collection)
}

fun ImageView.loadImageWithGlide(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadImageWithGlide(drawable: Drawable?) {
    Glide.with(context)
        .load(drawable)
        .into(this)
}

fun ImageView.loadImageWithGlide(@DrawableRes drawableRes: Int) {
    Glide.with(context)
        .load(drawableRes)
        .into(this)
}


fun ImageView.loadImageWithGlideBlur(url: String, radius: Int = 50, sampling: Int = 5) {
    Glide.with(context)
        .load(url)
        .transform(BlurTransformation(radius, sampling))
        .into(this)
}

fun RecyclerView.scrollToLast(delay: Long = 250L) {
    val pos = adapter?.itemCount ?: IntConstants.ZERO - IntConstants.ONE
    if (pos > IntConstants.ZERO) {
        postDelayed(delay) {
            if (!isAtLastPosition()) {
                this@scrollToLast.smoothScrollToPosition(pos)
            }
        }
    }
}

fun RecyclerView.isAtLastPosition(): Boolean {
    val itemCount = this.adapter?.itemCount ?: IntConstants.ZERO
    val layoutManager = layoutManager as LinearLayoutManager
    val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
    val pos = itemCount - IntConstants.ONE
    return lastVisiblePosition == pos
}

fun RecyclerView.scrollToFirst(delay: Long = 250L) {
    if ((this.adapter?.itemCount ?: 0) == IntConstants.ZERO) return
    postDelayed(delay) {
        this@scrollToFirst.smoothScrollToPosition(IntConstants.ZERO)
    }
}

fun Context.isJobServiceOn(jobId: Int): Boolean {
    val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    var hasBeenScheduled = false
    for (jobInfo in scheduler.allPendingJobs) {
        if (jobInfo.id == jobId) {
            hasBeenScheduled = true
            break
        }
    }
    return hasBeenScheduled
}