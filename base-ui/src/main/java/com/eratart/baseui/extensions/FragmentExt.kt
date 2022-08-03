package com.eratart.baseui.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T = apply {
    arguments = Bundle().apply(argsBuilder)
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.argument(key: String) = arguments?.get(key) as T?

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.argument(key: String, default: T) = arguments?.get(key) as T? ?: default

fun <T> Fragment.lazyArgument(key: String) = lazy { argument<T>(key) }

fun <T> Fragment.lazyArgument(key: String, default: T) = lazy { argument(key, default) }