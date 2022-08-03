package com.eratart.feature.item.creation.view.size

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.withArgs
import com.eratart.core.constants.IntConstants
import com.eratart.domain.model.domain.Size
import com.eratart.domain.model.enums.SizeType
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetSizeFragmentBinding
import com.eratart.feature.item.creation.view.size.adapter.SizeAdapter
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SizeBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetSizeFragmentBinding>(R.layout.bottom_sheet_size_fragment) {

    companion object {
        private const val ARGS_SIZE = "SizeBottomSheetFragment.ARGS_SIZE"
        private const val TAG = "SizeBottomSheetFragment"

        fun newInstance(selectedSize: Size?) =
            SizeBottomSheetFragment().withArgs {
                putParcelable(ARGS_SIZE, selectedSize)
            }
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetSizeFragmentBinding>()

    private val sizes = getEuSizes()
    private val sizesAdapter by lazy { SizeAdapter(sizes) }

    private val size by lazyArgument<Size>(ARGS_SIZE)
    private var currentSize: Size? = null

    private var isSizeInitialized = false

    private fun getNames() = listOf(
        SizeType.EUR.name, SizeType.US.name
    )

    override fun initView() {
        with(binding) {
            rvSizes.apply {
                val manager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                layoutManager = manager
                adapter = sizesAdapter
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(this)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            snapHelper.findSnapView(manager)?.apply {
                                val pos = manager.getPosition(this)
                                currentSize = sizes[pos]
                                updateSizeText()
                            }
                        }
                    }
                })
            }

            tlSizes.initTabLayout(getNames()) {
                if (isSizeInitialized) {
                    if (it == IntConstants.ZERO) {
                        sizes.replaceAllWith(getEuSizes())
                    } else {
                        sizes.replaceAllWith(getUsSizes())
                    }
                    sizesAdapter.notifyDataSetChanged()
                    val pos = sizes.size / 2
                    if (sizes.size > pos) {
                        binding.rvSizes.smoothScrollToPosition(pos)
                    }
                }
            }
            initCurrentSize()
            btnSelect.setOnClickListener {
                selectSize()
            }
        }
    }

    private fun initCurrentSize() {
        val initSize = size
        if (initSize != null) {
            when (initSize.sizeType) {
                SizeType.EUR -> {
                    val pos = sizes.indexOf(initSize)
                    if (pos != IntConstants.MINUS_ONE) {
                        binding.rvSizes.smoothScrollToPosition(pos)
                    }
                }
                SizeType.US -> {
                    binding.tlSizes.tabLayout.getTabAt(IntConstants.ONE)?.select()
                    sizes.replaceAllWith(getUsSizes())
                    sizesAdapter.notifyDataSetChanged()

                    val pos = sizes.indexOf(initSize)
                    if (pos != IntConstants.MINUS_ONE) {
                        binding.rvSizes.smoothScrollToPosition(pos)
                    }
                }
                else -> {}
            }
        } else {
            val pos = sizes.size / 2
            if (sizes.size > pos) {
                binding.rvSizes.smoothScrollToPosition(pos)
            }
        }
        isSizeInitialized = true
    }

    private fun updateSizeText() {
        binding.tvCurrentSize.text = currentSize?.size
    }

    private fun selectSize() {
        viewModel.selectSize(currentSize)
        dismiss()
    }

    private fun getEuSizes(): ArrayList<Size> {
        val sizes = ArrayList<Size>()
        val startSize = 30
        val sizeCount = 15
        val sizeType = SizeType.EUR
        for (i in 0..sizeCount) {
            sizes.add(Size((startSize + i * 2).toString(), sizeType))
        }
        return sizes
    }

    private fun getUsSizes(): ArrayList<Size> {
        val sizes = ArrayList<Size>()
        val startSize = 50
        val sizeCount = 15
        val sizeType = SizeType.US
        for (i in 0..sizeCount) {
            sizes.add(Size((startSize + i * 3).toString(), sizeType))
        }
        return sizes
    }
}