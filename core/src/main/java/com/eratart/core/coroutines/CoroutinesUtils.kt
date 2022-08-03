package com.eratart.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CoroutinesUtils {
    suspend fun <T> withUi(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.Main, block)

    suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.IO, block)
}