package com.eratart.feature.item.creation.view.brand

import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.withArgs
import com.eratart.domain.model.domain.Brand
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetBrandFragmentBinding
import com.eratart.feature.item.creation.view.brand.adapter.BrandAdapter
import com.eratart.feature.item.creation.view.brand.entity.BrandEntity
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrandBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetBrandFragmentBinding>(R.layout.bottom_sheet_brand_fragment),
    BrandAdapter.IBrandListener {

    companion object {
        private const val ARGS_BRANDS = "BrandBottomSheetFragment.ARGS_BRANDS"
        private const val ARGS_BRAND = "BrandBottomSheetFragment.ARGS_BRAND"
        private const val TAG = "BrandBottomSheetFragment"

        fun newInstance(brands: ArrayList<Brand>, selectedBrand: Brand?) =
            BrandBottomSheetFragment().withArgs {
                putParcelableArrayList(ARGS_BRANDS, brands)
                putParcelable(ARGS_BRAND, selectedBrand)
            }
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetBrandFragmentBinding>()

    private val brands by lazyArgument<ArrayList<Brand>>(ARGS_BRANDS, arrayListOf())
    private val selectedBrand by lazyArgument<Brand>(ARGS_BRAND)

    private val brandsList = ArrayList<BrandEntity>()
    private var currentSelectedBrand: BrandEntity? = null

    private val brandsAdapter by lazy { BrandAdapter(brandsList, this) }

    override fun initView() {
        with(binding) {
            selectedBrand?.apply {
                currentSelectedBrand = BrandEntity(this, true, null)
                btnSelect.setActive(true)
            }
            btnBack.setOnClickListener { dismiss() }

            rvBrands.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = brandsAdapter
            }

            btnSelect.setOnClickListener {
                viewModel.selectBrand(currentSelectedBrand?.brand)
                dismiss()
            }

            val sortedBrands = brands.sortedBy { it.name }

            val mappedBrands = ArrayList<BrandEntity>()
            sortedBrands.forEach {
                val firstLetter = it.name.first().toString()

                val containsFirstLetter = mappedBrands
                    .map { mappedBrand -> mappedBrand.title }
                    .contains(firstLetter)

                if (!containsFirstLetter) {
                    mappedBrands.add(BrandEntity(null, false, firstLetter))
                }
                mappedBrands.add(BrandEntity(it, it == selectedBrand, null))
            }

            brandsList.replaceAllWith(mappedBrands)
            brandsAdapter.notifyDataSetChanged()
        }
    }

    override fun onLayoutReady() {
        super.onLayoutReady()
        requireActivity().apply {
            binding.rvBrands.setHeight(getScreenHeight() - getStatusBarHeight() - PADDING_TOP)
        }
    }

    override fun onBrandClick(brandEntity: BrandEntity) {
        binding.btnSelect.setActive(true)
        brandsList.forEachIndexed { index, entity ->
            binding.btnSelect.setActive(true)
            currentSelectedBrand = brandEntity
            if (entity.brand?.id == brandEntity.brand?.id) {
                if (!entity.isSelected) {
                    brandsList[index] = brandsList[index].copy(isSelected = true)
                }
            } else {
                if (entity.isSelected) {
                    brandsList[index] = brandsList[index].copy(isSelected = false)
                }
            }
        }
        brandsAdapter.notifyDataSetChanged()
    }
}