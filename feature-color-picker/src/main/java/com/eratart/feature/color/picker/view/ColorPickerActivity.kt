package com.eratart.feature.color.picker.view

import android.app.Activity
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.visible
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Colors
import com.eratart.feature.color.picker.R
import com.eratart.feature.color.picker.databinding.ActivityColorPickerBinding
import com.eratart.feature.color.picker.di.ColorPickerModule
import com.eratart.feature.color.picker.entity.ColorPickerEntity
import com.eratart.feature.color.picker.entity.ColorPickerEntityType
import com.eratart.feature.color.picker.view.color.ColorBottomSheetFragment
import com.eratart.feature.color.picker.viewmodel.ColorPickerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorPickerActivity :
    BaseActivity<ColorPickerViewModel, ActivityColorPickerBinding>(R.layout.activity_color_picker) {

    companion object {
        private const val EXTRAS_COLORS = "ColorPickerActivity.EXTRAS_COLORS"
        private const val RESULT_COLORS = "ColorPickerActivity.RESULT_COLORS"

        fun newInstance(activity: Activity, colors: Colors?): Intent {
            return Intent(activity, ColorPickerActivity::class.java).apply {
                putExtra(EXTRAS_COLORS, colors)
            }
        }

        fun parseResult(resultCode: Int, intent: Intent?, default: Colors?): Colors? {
            return when (resultCode) {
                RESULT_OK -> intent?.getParcelableExtra(RESULT_COLORS)
                RESULT_CANCELED -> default
                else -> null
            }
        }
    }

    private val colors by lazy { intent.getParcelableExtra<Colors>(EXTRAS_COLORS) }

    override val koinModules = listOf(ColorPickerModule)
    override val viewModel: ColorPickerViewModel by viewModel()
    override val binding: ActivityColorPickerBinding by viewBinding()
    private val btnBack by lazy { binding.btnBack }
    private val ivImage by lazy { binding.ivImage }
    private val btnSelect by lazy { binding.btnSelect }
    private val sivColorMain by lazy { binding.sivColorMain }
    private val sivColorAdditional1 by lazy { binding.sivColorAdditional1 }
    private val sivColorAdditional2 by lazy { binding.sivColorAdditional2 }

    private var resultColors: Colors? = null

    private var mainColor: Int? = null
    private var additionalColor1: Int? = null
    private var additionalColor2: Int? = null

    override fun initView() {
        resultColors = colors
        btnBack.setOnClickListener {
            onBackPressed()
        }
        ivImage.setImageURI(colors?.imageUri)
        sivColorMain.setOnClickListener {
            val color = resultColors?.mainColor
            launchBottomSheetFragment(color, ColorPickerEntityType.MAIN_COLOR)
        }
        sivColorAdditional1.setOnClickListener {
            val color = resultColors?.additionalColors?.getOrNull(IntConstants.ZERO)
            launchBottomSheetFragment(color, ColorPickerEntityType.ADDITIONAL_COLOR_1)
        }
        sivColorAdditional2.setOnClickListener {
            val color = resultColors?.additionalColors?.getOrNull(IntConstants.ONE)
            launchBottomSheetFragment(color, ColorPickerEntityType.ADDITIONAL_COLOR_2)
        }

        btnSelect.setOnClickListener { handleNewResult() }
        val additionalColorPrefix = getString(R.string.feature_color_picker_additional_color)
            .plus(StringConstants.SPACE)
        sivColorAdditional1.setTitle(additionalColorPrefix.plus(IntConstants.ONE))
        sivColorAdditional2.setTitle(additionalColorPrefix.plus(IntConstants.TWO))

        handleMainColor(resultColors?.mainColor)
        handleAdditionalColor1(resultColors?.additionalColors?.getOrNull(IntConstants.ZERO))
        handleAdditionalColor2(resultColors?.additionalColors?.getOrNull(IntConstants.ONE))
    }

    private fun launchBottomSheetFragment(color: Int?, type: ColorPickerEntityType) {
        val entity = ColorPickerEntity(color, type)
        ColorBottomSheetFragment.newInstance(entity).show(supportFragmentManager)
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(mainColor, ::handleMainColor)
            registerObserver(additionalColor1, ::handleAdditionalColor1)
            registerObserver(additionalColor2, ::handleAdditionalColor2)
        }
    }

    private fun handleMainColor(color: Int?) {
        mainColor = color
        if (color != null) {
            sivColorMain.setItems(listOf(color))
            sivColorAdditional1.visible()
        } else {
            sivColorMain.setItems(listOf())
            sivColorAdditional1.gone()
        }
        btnSelect.setActive(true)
    }

    private fun handleAdditionalColor1(color: Int?) {
        additionalColor1 = color
        if (color != null) {
            sivColorAdditional1.setItems(listOf(color))
            sivColorAdditional2.visible()
        } else {
            sivColorAdditional1.setItems(listOf())
            sivColorAdditional2.gone()
        }
    }

    private fun handleAdditionalColor2(color: Int?) {
        additionalColor2 = color
        if (color != null) {
            sivColorAdditional2.setItems(listOf(color))
        } else {
            sivColorAdditional2.setItems(listOf())
        }
    }

    private fun handleNewResult() {
        resultColors = resultColors?.copy(mainColor = mainColor)
        val list = mutableListOf<Int>()
        additionalColor1?.apply { list.add(this) }
        additionalColor2?.apply { list.add(this) }
        resultColors = resultColors?.copy(additionalColors = list)
        intent.putExtra(RESULT_COLORS, resultColors)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        super.onBackPressed()
    }
}