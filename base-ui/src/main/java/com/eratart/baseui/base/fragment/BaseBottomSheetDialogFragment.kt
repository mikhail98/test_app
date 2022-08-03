package com.eratart.baseui.base.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.dpToPx
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(layoutId: Int) : AppCompatDialogFragment(layoutId) {

    companion object{
        val PADDING_TOP = 256f.dpToPx()
    }

    abstract val binding: VB?
    abstract val viewModel: BaseViewModel
    abstract val fragmentTag: String

    protected open var defaultState = BottomSheetBehavior.STATE_EXPANDED

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), getCustomTheme()).apply {
            behavior.state = defaultState
            setOnShowListener { makeTransparent(it) }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addOnLayoutReadyListener()
    }

    abstract fun initView()

    protected open fun getCustomTheme() = theme

    private fun makeTransparent(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) ?: return
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, fragmentTag)
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