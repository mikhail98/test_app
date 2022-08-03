package com.eratart.baseui.base.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.eratart.baseui.exception.BaseFailure
import com.eratart.baseui.exception.DefaultFailure
import com.eratart.baseui.exception.NoNetworkFailure
import com.eratart.baseui.exception.UnauthorizedFailure
import com.eratart.core.coroutines.applyBeforeAfterUi
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.core.coroutines.onNextUi
import com.eratart.core.ext.printError
import com.eratart.domain.exception.ApiException
import com.eratart.domain.exception.UnauthorizedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext = Dispatchers.IO

    open fun onCreate() {}

    private val _closeScreen = MutableSharedFlow<Boolean>()
    val closeScreen = _closeScreen.asSharedFlow()

    private val _failure = MutableSharedFlow<BaseFailure>()
    val failure = _failure.asSharedFlow()

    private val _messageToast = MutableSharedFlow<String>()
    val messageToast = _messageToast.asSharedFlow()

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()

    private val errorRenderPublisher = MutableSharedFlow<String>()

    init {
        subscribeOnMessageTrigger()
    }

    fun closeActivity() {
        launch { _closeScreen.emit(true) }
    }

    fun triggerMessage(message: String) {
        launch { errorRenderPublisher.emit(message) }
    }

    private fun subscribeOnMessageTrigger() {
        launchFlow {
            errorRenderPublisher
                .subscribeWithError { message -> handleToast(message) }
        }
    }

    @CallSuper
    protected open fun handleError(throwable: Throwable) {
        setLoading(false)
        val failure = when (throwable) {
            is UnauthorizedException -> UnauthorizedFailure()
            is ApiException -> NoNetworkFailure()
            else -> DefaultFailure()
        }
        handleFailure(failure)
    }

    protected fun handleFailure(failure: BaseFailure) {
        launch { _failure.emit(failure) }
    }

    protected fun handleToast(message: String) {
        launch { _messageToast.emit(message) }
    }

    open fun onDestroy() {
        coroutineContext.cancelChildren()
    }

    protected fun setLoading(value: Boolean) {
        launch { setLoadingSuspend(value) }
    }

    protected suspend fun setLoadingSuspend(value: Boolean) {
        _isLoading.emit(value)
    }

    protected fun <T> Flow<T>.applyLoader(): Flow<T> {
        return this.applyBeforeAfterUi({ setLoadingSuspend(true) }, { setLoadingSuspend(false) })
    }

    fun <T : Any> Flow<T>.subscribeWithError(listener: suspend (T) -> Unit = {}) =
        this
            .onNextUi { result ->
                setLoading(false)
                listener.invoke(result)
            }
            .catchUi { error ->
                error.printError()
                handleError(error)
            }
}