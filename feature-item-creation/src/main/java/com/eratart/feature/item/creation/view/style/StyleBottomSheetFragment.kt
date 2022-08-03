package com.eratart.feature.item.creation.view.style

import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.withArgs
import com.eratart.domain.model.domain.Style
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetStyleFragmentBinding
import com.eratart.feature.item.creation.view.style.adapter.StyleAdapter
import com.eratart.feature.item.creation.view.style.entity.StyleEntity
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StyleBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetStyleFragmentBinding>(R.layout.bottom_sheet_style_fragment) {

    companion object {
        private const val ARGS_STYLES = "StyleBottomSheetFragment.ARGS_STYLES"
        private const val ARGS_STYLE = "StyleBottomSheetFragment.ARGS_STYLE"
        private const val TAG = "StyleBottomSheetFragment"

        fun newInstance(styles: ArrayList<Style>, selectedStyle: Style?) =
            StyleBottomSheetFragment().withArgs {
                putParcelableArrayList(ARGS_STYLES, styles)
                putParcelable(ARGS_STYLE, selectedStyle)
            }
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetStyleFragmentBinding>()

    private val styles by lazyArgument<ArrayList<Style>>(ARGS_STYLES, arrayListOf())
    private val selectedStyle by lazyArgument<Style>(ARGS_STYLE)

    private val stylesList = ArrayList<StyleEntity>()
    private var currentSelectedStyle: StyleEntity? = null

    private val categoriesAdapter by lazy { StyleAdapter(stylesList) }

    override fun initView() {
        with(binding) {
            selectedStyle?.apply {
                currentSelectedStyle = StyleEntity(this, true)
                btnSelect.setActive(true)
            }
            btnBack.setOnClickListener { dismiss() }

            rvStyles.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = categoriesAdapter
                categoriesAdapter.setOnItemClick { categoryEntity, i ->
                    stylesList.forEachIndexed { index, entity ->
                        btnSelect.setActive(true)
                        currentSelectedStyle = categoryEntity
                        if (entity.style.id == categoryEntity.style.id) {
                            if (!entity.isSelected) {
                                stylesList[index] =
                                    stylesList[index].copy(isSelected = true)

                            }
                        } else {
                            if (entity.isSelected) {
                                stylesList[index] =
                                    stylesList[index].copy(isSelected = false)
                            }
                        }
                    }
                    categoriesAdapter.notifyDataSetChanged()
                }
            }

            btnSelect.setOnClickListener {
                viewModel.selectStyle(currentSelectedStyle?.style)
                dismiss()
            }
            val mappedCategories = styles.map {
                StyleEntity(it, it == selectedStyle)
            }
            stylesList.replaceAllWith(mappedCategories)
            categoriesAdapter.notifyDataSetChanged()
        }
    }

    override fun onLayoutReady() {
        super.onLayoutReady()
        requireActivity().apply {
            binding.rvStyles.setHeight(getScreenHeight() - getStatusBarHeight() - PADDING_TOP)
        }
    }
}