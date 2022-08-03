package com.eratart.baseui.base.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.exception.BaseFailure
import com.eratart.navigator.api.IGlobalNavigator
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>(@LayoutRes layoutRes: Int) :
    Fragment(layoutRes) {

    protected val navigator: IGlobalNavigator by inject()

    abstract val binding: VB?
    abstract val viewModel: VM

    protected open fun showLoader() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addOnLayoutReadyListener()
    }

    abstract fun initFragment()

    abstract fun initView()

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
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    protected fun showSnackbar(text: String) {
        binding?.root?.apply {
            Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showSnackbar(@StringRes textId: Int) {
        showSnackbar(getString(textId))
    }

    private fun addOnLayoutReadyListener() {
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                onLayoutReady()
                binding?.root?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        }
        binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(listener)
    }

    protected open fun onLayoutReady() {}
}