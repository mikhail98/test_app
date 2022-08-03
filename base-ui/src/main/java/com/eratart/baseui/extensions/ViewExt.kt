package com.eratart.baseui.extensions

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.eratart.baseui.extensions.ViewExtConstants.DURATION_DEBOUNCE_CLICK
import com.eratart.baseui.extensions.ViewExtConstants.DURATION_DEFAULT_GONE
import com.eratart.baseui.extensions.ViewExtConstants.DURATION_DEFAULT_VISIBLE
import com.eratart.baseui.extensions.ViewExtConstants.DURATION_PULSE_RECORDING
import com.eratart.baseui.extensions.ViewExtConstants.PROPERTY_NAME_ALPHA
import com.eratart.baseui.view.edittext.listeners.TextListener
import com.eratart.baseui.view.viewpager.ViewPageScroller
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.LongConstants
import com.eratart.core.ext.printError
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.Field

object ViewExtConstants {

    const val DURATION_DEFAULT_GONE = 150L
    const val DURATION_DEFAULT_VIBRATE = 150L
    const val DURATION_DEFAULT_VISIBLE = 300L
    const val DURATION_DEBOUNCE_CLICK = 500L
    const val DURATION_DEBOUNCE_CHECK_SWITCH = 500L
    const val DURATION_PULSE_RECORDING = 600L

    const val PROPERTY_NAME_ALPHA = "alpha"
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setVisible(flag: Boolean) {
    if (flag) visible() else gone()
}

fun View.setVisibleAlpha(flag: Boolean) {
    if (flag) visibleWithAlpha() else goneWithAlpha()
}

fun TextView.setTextAnimation(
    text: String, duration: Long = 300, completion: (() -> Unit)? = null,
) {
    if (this.text == text) return
    fadeOutAnimation(duration) {
        this.text = text
        fadeInAnimation(duration) {
            completion?.invoke()
        }
    }
}

fun TextView.setGoneIfNull(text: String?) {
    text?.let { this.text = it }
    isVisible = text != null
}

fun TextView.setTextAnimation(
    @StringRes textRes: Int, duration: Long = 300, completion: (() -> Unit)? = null,
) {
    setTextAnimation(context.getString(textRes), duration, completion)
}

fun View.fadeOutAnimation(
    duration: Long = 150,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null,
) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadeInAnimation(duration: Long = 150, completion: (() -> Unit)? = null) {
    alpha = FloatConstants.ZERO
    visibility = View.VISIBLE
    animate()
        .alpha(FloatConstants.ONE)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}

fun ViewPager2.setCurrentItemAnimated(
    item: Int,
    duration: Long = 350L,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    pagePxWidth: Int = width,
) {
    val pxToDrag: Int = pagePxWidth * (item - currentItem)
    val animator = ValueAnimator.ofInt(IntConstants.ZERO, pxToDrag)
    var previousValue = IntConstants.ZERO
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(-currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            beginFakeDrag()
        }

        override fun onAnimationEnd(animation: Animator?) {
            endFakeDrag()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}

fun <T : View> T.applyPost(block: T.() -> Unit) {
    post { block() }
}

fun View.visibleWithAlpha(duration: Long = DURATION_DEFAULT_VISIBLE) {
    if (this.visibility != View.VISIBLE) {
        this.alpha = FloatConstants.ZERO
        this.visible()
        this.animate().alpha(FloatConstants.ONE).setDuration(duration).start()
    }
}

fun View.goneWithAlpha(duration: Long = DURATION_DEFAULT_GONE) {
    if (this.visibility == View.VISIBLE) {
        this.alpha = FloatConstants.ONE
        this.animate().alpha(FloatConstants.ZERO).setDuration(duration).start()
        postDelayed(duration) { this.gone() }
    }
}

fun View.addScaleAnimation() {
    alpha = FloatConstants.ONE
    val scaleDown = android.animation.ObjectAnimator.ofPropertyValuesHolder(
        this,
        android.animation.PropertyValuesHolder.ofFloat(PROPERTY_NAME_ALPHA, FloatConstants.ZERO)
    )
    scaleDown.duration = DURATION_PULSE_RECORDING
    scaleDown.repeatCount = android.animation.ObjectAnimator.INFINITE
    scaleDown.repeatMode = android.animation.ObjectAnimator.REVERSE
    scaleDown.start()
}

fun View.hideKeyboardFrom() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, IntConstants.ZERO)
}

fun View.showKeyboardFrom() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, IntConstants.ZERO)
}

fun View.isKeyboardActive(): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isAcceptingText
}

fun View.throttleClickListener(
    duration: Long = DURATION_DEBOUNCE_CLICK,
    listener: (View) -> Unit,
) {
    callbackFlow {
        setOnClickListener { trySendBlocking(this@throttleClickListener) }
        awaitClose { setOnClickListener(null) }
    }
        .throttleFirst(duration)
        .onEach { item -> listener.invoke(item) }
        .launchIn(CoroutineScope(Dispatchers.Main))
}

fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = LongConstants.ZERO
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        val mayEmit = currentTime - lastEmissionTime > windowDuration
        if (mayEmit) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}

fun Int.pxToDp(): Float {
    return this.toFloat() / Resources.getSystem().displayMetrics.density
}

fun Float.pxToDp(): Float {
    return this / Resources.getSystem().displayMetrics.density
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Float.dpToPx(): Float {
    return this * Resources.getSystem().displayMetrics.density
}

fun RecyclerView.addOnScrollLoadListener(listener: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            layoutManager?.apply {
                this as LinearLayoutManager
                val lastPosition = findLastVisibleItemPosition()
                if (lastPosition >= itemCount - IntConstants.ONE) {
                    listener.invoke()
                }
            }
        }
    })
}

fun TextView.setHtmlWithLinks(@StringRes textRes: Int) {
    setHtmlWithLinks(context.getString(textRes))
}

fun TextView.setHtmlWithLinks(sourceText: String) {
    apply {
        val htmlText = Html.fromHtml(sourceText, Html.FROM_HTML_MODE_LEGACY) as Spannable
        setLinkTextColor(
            ContextCompat.getColor(context, com.eratart.baseui.R.color.eminem)
        )
        movementMethod = LinkMovementMethod.getInstance()

        for (u in htmlText.getSpans(0, htmlText.length, URLSpan::class.java)) {
            htmlText.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, htmlText.getSpanStart(u), htmlText.getSpanEnd(u), 0)
        }
        text = htmlText
    }
}

fun View.setHeight(height: Float) {
    setHeight(height.toInt())
}

fun View.setWidth(width: Float) {
    setWidth(width.toInt())
}

fun View.setHeight(height: Int) {
    val viewParams = this.layoutParams as ConstraintLayout.LayoutParams
    viewParams.height = height
    this.layoutParams = viewParams
}

fun View.setWidth(width: Int) {
    val viewParams = this.layoutParams as ConstraintLayout.LayoutParams
    viewParams.width = width
    this.layoutParams = viewParams
}

fun View.setHeightFrame(height: Int) {
    val viewParams = this.layoutParams as FrameLayout.LayoutParams
    viewParams.height = height
    this.layoutParams = viewParams
}

fun View.setWidthFrame(width: Int) {
    val viewParams = this.layoutParams as FrameLayout.LayoutParams
    viewParams.width = width
    this.layoutParams = viewParams
}

fun ViewPager2.setViewPageScroller(viewPageScroller: ViewPageScroller) {
    try {
        val mScroller: Field = ViewPager::class.java.getDeclaredField("mScroller")
        mScroller.isAccessible = true
        mScroller.set(this, viewPageScroller)
    } catch (e: Exception) {
        e.printError()
    }
}

fun EditText.setOnTextChangedListener(listener: (String) -> Unit) {
    addTextChangedListener(TextListener { listener.invoke(it) })
}

fun EditText.setDebounceTextChangedListener(debounce: Long, listener: (String) -> Unit) {
    channelFlow {
        val textWatcher = TextListener { trySend(it) }
        addTextChangedListener(textWatcher)

        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }.debounce(debounce)
        .onEach { listener.invoke(it) }
        .launchIn(CoroutineScope(Dispatchers.Main))
}

fun NestedScrollView.addOnScrollListener(listener: (Int) -> Unit) {
    viewTreeObserver?.addOnScrollChangedListener {
        listener.invoke(scrollY)
    }
}

fun TabLayout.initTabLayout(names: List<String>, onTabChangedListener: (Int) -> Unit) {
    names.forEach {
        addTab(newTab().setText(it))
    }
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            onTabChangedListener.invoke(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    })
}