package com.eratart.core.ext

import com.eratart.core.BuildConfig
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.StringConstants
import java.util.*

fun Throwable.printError(isPrint: Boolean = BuildConfig.DEBUG) {
    if (isPrint) {
        println("RE:: ${this.localizedMessage ?: this.javaClass.name}")
        this.printStackTrace()
    }
}

inline fun <reified T : Enum<T>> String?.getEnumConstant(default: T) =
    if (this.isNullOrEmpty()) {
        default
    } else {
        try {
            enumValueOf(this)
        } catch (e: Exception) {
            println("RE:: ${e.localizedMessage ?: e.javaClass.name}")
            default
        }
    }

fun String.remove(pattern: String): String {
    return this.replace(pattern, StringConstants.EMPTY)
}

fun String.getDigits(): String {
    val digits = this.replace("\\D+".toRegex(), StringConstants.EMPTY)
    return if (digits.isEmpty()) IntConstants.ZERO.toString()
    else digits
}

fun String.getDigitsVersion(): Int {
    return try {
        val digits = this.replace("\\D+".toRegex(), StringConstants.SPACE).trim()
            .split(StringConstants.SPACE).map { item -> item.toInt() }
        val majorVersion = digits[IntConstants.ZERO] * 10000
        val minorVersion = digits[IntConstants.ONE] * 1000
        val patchVersion = digits[IntConstants.TWO]

        return if (digits.isEmpty()) IntConstants.ZERO
        else majorVersion + minorVersion + patchVersion
    } catch (e: Exception) {
        IntConstants.ZERO
    }
}

fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun tryThis(listener: () -> Unit) {
    try {
        listener.invoke()
    } catch (e: Exception) {
        e.printError()
    }
}