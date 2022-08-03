package com.eratart.feature.dialog.view

import android.app.Activity
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.feature.dialog.R
import com.eratart.feature.dialog.databinding.ActivityDialogBinding
import com.eratart.feature.dialog.di.DialogModule
import com.eratart.feature.dialog.viewmodel.DialogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogActivity :
    BaseActivity<DialogViewModel, ActivityDialogBinding>(R.layout.activity_dialog) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, DialogActivity::class.java)
        }
    }

    override val koinModules = listOf(DialogModule)
    override val viewModel: DialogViewModel by viewModel()
    override val binding: ActivityDialogBinding by viewBinding()

    override fun initView() {

    }

    override fun initViewModel() {

    }
}