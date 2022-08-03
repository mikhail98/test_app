package com.eratart.baseui.base.activity

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.eratart.baseui.R
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.exception.BaseFailure
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.baseui.view.other.DefaultFullscreenLoader
import com.eratart.navigator.api.IGlobalNavigator
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.core.scope.Scope

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding>(@LayoutRes layoutRes: Int) :
    AppCompatActivity(layoutRes), AndroidScopeComponent {

    abstract val binding: VB?
    abstract val viewModel: VM

    abstract fun initView()
    abstract fun initViewModel()

    override val scope: Scope by activityScope()

    protected val navigator: IGlobalNavigator by inject()

    protected val activityRootView by lazy { binding?.root }
    private lateinit var loader: DefaultFullscreenLoader

    protected open val koinModules: List<Module> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(koinModules)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        changeStatusBarColor()
        activityRootView?.run {
            setContentView(this)
        }
        initLoader()
        initActivity()
        addOnLayoutReadyListener()

        viewModel.apply {
            registerObserver(messageToast, ::handleToast)
            registerObserver(failure, ::renderFailure)
            registerObserver(closeScreen, ::handleCloseScreen)
            registerObserver(isLoading, ::renderLoader)
            onCreate()
        }
    }

    private fun initLoader() {
        loader = DefaultFullscreenLoader(this)
        with(loader) {
            if (activityRootView is ViewGroup) {
                (activityRootView as ViewGroup).addView(this)
            }
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            gone()
            bringToFront()
            setOnClickListener {  }
        }
    }

    protected fun renderLoader(isVisible: Boolean) {
        if (isVisible) {
            loader.visibleWithAlpha()
        } else {
            loader.gone()
        }
    }

    @CallSuper
    protected open fun initActivity() {
        initView()
        initViewModel()
    }

    protected fun changeStatusBarColor(color: Int = R.color.action_bar) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }


    override fun onDestroy() {
        unloadKoinModules(koinModules)
        viewModel.onDestroy()
        super.onDestroy()
    }

    private fun handleToast(message: String) {
        showToast(message)
    }

    protected open fun renderFailure(failure: BaseFailure) {
        viewModel.triggerMessage(getString(failure.errorTextRes))
    }

    private fun showToast(@StringRes textId: Int) {
        showToast(getString(textId))
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    protected fun showSnackbar(text: String) {
        activityRootView?.run {
            Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showSnackbar(@StringRes textId: Int) {
        showSnackbar(getString(textId))
    }

    private fun handleCloseScreen(close: Boolean) {
        if (close) {
            finish()
        }
    }

    protected open fun overrideAnimationAndClose() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun addOnLayoutReadyListener() {
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                onLayoutReady()
                activityRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        }
        activityRootView?.viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }

    protected open fun onLayoutReady() {}
}