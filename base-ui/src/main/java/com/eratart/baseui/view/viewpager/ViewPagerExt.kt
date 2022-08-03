package com.eratart.baseui.view.viewpager

import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.viewpager2.widget.ViewPager2
import com.eratart.baseui.extensions.addOnScrollListener
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.invisible
import com.eratart.baseui.extensions.visible
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.eratart.core.coroutines.onNext
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn

fun ViewPager2.setupWithNestedScroll(nestedScrollView: NestedScrollView, scrollLimit: Int) {
    var isFirstSelect = true
    addChangeListener {
        if (isFirstSelect) {
            isFirstSelect = false
        } else {
            onPageSelected(nestedScrollView, it, scrollLimit)
        }
    }
}

private fun ViewPager2.onPageSelected(
    nestedScrollView: NestedScrollView, position: Int, scrollLimit: Int,
) {
    val view = (adapter as BaseViewPager2Adapter).getViewAtPosition(position)
    updatePagerHeightForChild(view)
    if ((nestedScrollView.scrollY) > scrollLimit) {
        nestedScrollView.scrollY = scrollLimit
    }
}

private fun ViewPager2.updatePagerHeightForChild(view: View?) {
    view?.post {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            view.width, View.MeasureSpec.EXACTLY
        )
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            IntConstants.ZERO, View.MeasureSpec.UNSPECIFIED
        )
        view.measure(wMeasureSpec, hMeasureSpec)

        if (layoutParams.height != view.measuredHeight) {
            layoutParams = layoutParams.also {
                it.height = view.measuredHeight
            }
        }
    }
}

fun ViewPager2.setupWithHackTL(
    tabLayout: TabLayout,
    hackTabLayout: TabLayout,
    names: List<String>,
    nestedScrollView: NestedScrollView,
    scrollLimit: Int,
    hackView: View? = null,
    listener: (alpha: Float) -> Unit = {},
) {
    setUpWithTabLayout(tabLayout, names)
    setUpWithTabLayout(hackTabLayout, names)
    nestedScrollView.addScrollLimitListener(scrollLimit) {
        val viewToHide = hackView ?: hackTabLayout
        if (it == FloatConstants.ONE) {
            if (!viewToHide.isVisible) {
                viewToHide.visible()
                tabLayout.invisible()
            }
        } else {
            if (viewToHide.isVisible) {
                viewToHide.gone()
                tabLayout.visible()
            }
        }
        listener.invoke(it)
    }
}

fun NestedScrollView.addScrollLimitListener(
    scrollLimit: Int,
    listener: (alpha: Float) -> Unit = {},
) {
    addOnScrollListener { scrollY ->
        if (scrollLimit != IntConstants.ZERO) {
            var alpha = scrollY.toFloat() / scrollLimit

            if (alpha > FloatConstants.ONE) {
                alpha = FloatConstants.ONE
            }
            listener.invoke(alpha)
        }
    }
}

fun ViewPager2.addDebounceChangeListener(listener: (Int) -> Unit) = callbackFlow {
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int,
        ) {
        }

        override fun onPageSelected(position: Int) {
            trySendBlocking(position)
        }
    }
    registerOnPageChangeCallback(callback)
    awaitClose {
        unregisterOnPageChangeCallback(callback)
    }
}.debounce(250L)
    .onNext { item -> listener.invoke(item) }
    .launchIn(CoroutineScope(Dispatchers.Main))

fun ViewPager2.addChangeListener(listener: (Int) -> Unit) {
    val callback = getViewPager2Listener(listener)
    registerOnPageChangeCallback(callback)
}

fun getViewPager2Listener(listener: (Int) -> Unit): ViewPager2.OnPageChangeCallback {
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int,
        ) {
        }

        override fun onPageSelected(position: Int) {
            listener.invoke(position)
        }
    }
    return callback
}

fun ViewPager2.setUpWithTabLayout(tabLayout: TabLayout?, titles: List<String>) {
    tabLayout?.let { tl ->
        TabLayoutMediator(tl, this) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}

fun ViewPager2.setTabLayoutClickable(
    tabLayout: TabLayout?,
    titles: List<String>,
    isClickable: Boolean,
) {
    tabLayout?.let { tl ->
        TabLayoutMediator(tl, this) { tab, position ->
            tab.text = titles[position]
            tab.view.isClickable = isClickable
        }.attach()
    }
}
