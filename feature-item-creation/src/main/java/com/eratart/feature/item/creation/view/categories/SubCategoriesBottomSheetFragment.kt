package com.eratart.feature.item.creation.view.categories

import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.withArgs
import com.eratart.domain.model.domain.Category
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetSubCategoriesFragmentBinding
import com.eratart.feature.item.creation.view.categories.adapter.CategoriesAdapter
import com.eratart.feature.item.creation.view.categories.entity.CategoryEntity
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubCategoriesBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetSubCategoriesFragmentBinding>(R.layout.bottom_sheet_sub_categories_fragment) {

    companion object {
        private const val ARGS_CATEGORIES = "SubCategoriesBottomSheetFragment.ARGS_CATEGORIES"
        private const val ARGS_CATEGORY = "SubCategoriesBottomSheetFragment.ARGS_CATEGORY"
        private const val TAG = "SubCategoriesBottomSheetFragment"

        fun newInstance(categories: ArrayList<Category>, selectedCategory: Category?) =
            SubCategoriesBottomSheetFragment().withArgs {
                putParcelableArrayList(ARGS_CATEGORIES, categories)
                putParcelable(ARGS_CATEGORY, selectedCategory)
            }
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetSubCategoriesFragmentBinding>()

    private val categories by lazyArgument<ArrayList<Category>>(ARGS_CATEGORIES, arrayListOf())
    private val selectedCategory by lazyArgument<Category>(ARGS_CATEGORY)

    private val categoriesList = ArrayList<CategoryEntity>()
    private var currentSelectedCategory: CategoryEntity? = null

    private val categoriesAdapter by lazy { CategoriesAdapter(categoriesList) }

    override fun initView() {
        with(binding) {
            selectedCategory?.apply {
                currentSelectedCategory = CategoryEntity(this, true)
                btnSelect.setActive(true)
            }
            btnBack.setOnClickListener { dismiss() }

            rvCategories.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = categoriesAdapter
                categoriesAdapter.setOnItemClick { categoryEntity, i ->
                    categoriesList.forEachIndexed { index, entity ->
                        btnSelect.setActive(true)
                        currentSelectedCategory = categoryEntity
                        if (entity.category.id == categoryEntity.category.id) {
                            if (!entity.isSelected) {
                                categoriesList[index] =
                                    categoriesList[index].copy(isSelected = true)

                            }
                        } else {
                            if (entity.isSelected) {
                                categoriesList[index] =
                                    categoriesList[index].copy(isSelected = false)
                            }
                        }
                    }
                    categoriesAdapter.notifyDataSetChanged()
                }
            }

            btnSelect.setOnClickListener {
                viewModel.selectSubCategory(currentSelectedCategory?.category)
                dismiss()
            }
            val mappedCategories = categories.map {
                CategoryEntity(it, it == selectedCategory)
            }
            categoriesList.replaceAllWith(mappedCategories)
            categoriesAdapter.notifyDataSetChanged()
        }
    }

    override fun onLayoutReady() {
        super.onLayoutReady()

        requireActivity().apply {
            binding.rvCategories.setHeight(getScreenHeight() - getStatusBarHeight() - PADDING_TOP)
        }
    }
}