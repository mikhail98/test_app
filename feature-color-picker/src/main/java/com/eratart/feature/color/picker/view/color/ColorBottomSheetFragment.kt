package com.eratart.feature.color.picker.view.color

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.withArgs
import com.eratart.baseui.view.other.ColorRadioButton
import com.eratart.feature.color.picker.R
import com.eratart.feature.color.picker.databinding.BottomSheetColorFragmentBinding
import com.eratart.feature.color.picker.entity.ColorPickerEntity
import com.eratart.feature.color.picker.entity.ColorPickerEntityType
import com.eratart.feature.color.picker.viewmodel.ColorPickerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetColorFragmentBinding>(R.layout.bottom_sheet_color_fragment) {

    companion object {
        private const val TAG = "ColorBottomSheetFragment"

        private const val ARGS_SELECTED_COLOR = "ColorBottomSheetFragment.ARGS_SELECTED_COLOR"

        fun newInstance(colorPickerEntity: ColorPickerEntity?) =
            ColorBottomSheetFragment().withArgs {
                putParcelable(ARGS_SELECTED_COLOR, colorPickerEntity)
            }
    }

    override val fragmentTag = TAG
    override val viewModel: ColorPickerViewModel by viewModel()
    override val binding by viewBinding<BottomSheetColorFragmentBinding>()

    private val selectedColor by lazyArgument<ColorPickerEntity>(ARGS_SELECTED_COLOR)
    private var currentSelectedColor: ColorPickerEntity? = ColorPickerEntity(null, null)

    private val cbList by lazy {
        with(binding) {
            listOf(
                crb1, crb2, crb3, crb4, crb5, crb6, crb7, crb8, crb9,
                crb10, crb11, crb12, crb13, crb14,
            )
        }
    }

    override fun initView() {
        currentSelectedColor = selectedColor
        cbList.forEach {
            it.addOnCheckChangeListener { isSelected, color ->
                onColorSelected(it)
            }
            if (it.getColor() == selectedColor?.selectedColor) {
                it.setItemSelected(true)
            }
        }
        binding.btnSelect.setOnClickListener {
            val color = currentSelectedColor?.selectedColor
            when (currentSelectedColor?.colorPickerEntityType) {
                ColorPickerEntityType.MAIN_COLOR -> viewModel.selectMainColor(color)
                ColorPickerEntityType.ADDITIONAL_COLOR_1 -> viewModel.selectAdditionalColor1(color)
                ColorPickerEntityType.ADDITIONAL_COLOR_2 -> viewModel.selectAdditionalColor2(color)
                else -> {}
            }
            dismiss()
        }
        updateBtnState()
    }

    private fun onColorSelected(button: ColorRadioButton) {
        cbList.forEach {
            if (it != button) {
                it.setItemSelected(false)
            } else {
                it.setItemSelected(true)
                currentSelectedColor = currentSelectedColor?.copy(selectedColor = it.getColor())
            }
        }
        updateBtnState()
    }

    private fun updateBtnState() {
        binding.btnSelect.setActive(currentSelectedColor?.selectedColor != null)
    }
}