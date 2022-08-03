package com.eratart.core.coroutines

import com.eratart.core.coroutines.CoroutinesUtils.withUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

fun <T> CoroutineScope.launchFlow(listener: suspend () -> Flow<T>) {
    this.launch { listener.invoke().launchIn(this) }
}

fun <T> Flow<T>.onNext(listener: suspend Flow<T>.(T) -> Unit): Flow<T> {
    return this.onEach { listener.invoke(this, it) }
}

fun <T> Flow<T>.onNextUi(listener: suspend Flow<T>.(T) -> Unit): Flow<T> {
    return onEach { withUi { listener.invoke(this@onNextUi, it) } }
}

fun <T> Throwable.asFlow() = flow<T> { throw this@asFlow }

fun <T> Flow<T>.applyBeforeAfter(
    actionStart: suspend () -> Unit,
    actionEnd: suspend () -> Unit = {},
): Flow<T> {
    return this.onStart { actionStart.invoke() }.onCompletion { actionEnd.invoke() }
}

fun <T> Flow<T>.applyBeforeAfterUi(
    actionStart: suspend () -> Unit,
    actionEnd: suspend () -> Unit = {},
): Flow<T> {
    return onStart { withUi { actionStart.invoke() } }.onCompletion { withUi { actionEnd.invoke() } }
}

fun <T> Flow<T>.catchUi(listener: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> {
    return catch { withUi { listener.invoke(this@catch, it) } }
}

fun <T> T.asFlow(): Flow<T> {
    return flow { emit(this@asFlow) }
}