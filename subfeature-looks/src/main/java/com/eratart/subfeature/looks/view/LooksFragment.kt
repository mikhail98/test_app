package com.eratart.subfeature.looks.view

import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.registerObserver
import com.eratart.core.constants.FloatConstants.ASPECT_RATIO_4_5
import com.eratart.domain.model.domain.Look
import com.eratart.subfeature.looks.R
import com.eratart.subfeature.looks.databinding.FragmentLooksBinding
import com.eratart.subfeature.looks.view.adapter.LooksAdapter
import com.eratart.subfeature.looks.viewmodel.LooksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LooksFragment : BaseFragment<LooksViewModel, FragmentLooksBinding>(R.layout.fragment_looks),
    LooksAdapter.IListener {

    companion object {
        fun newInstance(): LooksFragment {
            return LooksFragment()
        }
    }

    override val binding: FragmentLooksBinding by viewBinding()
    override val viewModel: LooksViewModel by viewModel()
    private val itemCardHeightMargin by lazy { 32f.dpToPx() }
    private val itemCardWidthMargin by lazy { 56f.dpToPx() }
    private val itemCardHeight by lazy { (requireContext().getScreenWidth() - itemCardHeightMargin) * ASPECT_RATIO_4_5 }
    private val itemCardWidth by lazy { requireContext().getScreenWidth() - itemCardWidthMargin }
    private val looksAdapter by lazy {
        LooksAdapter(mutableListOf(), itemCardHeight, itemCardWidth, this)
    }

    override fun initFragment() {
        viewModel.apply {
            registerObserver(looks, ::handleLooks)
        }
    }

    override fun onMoreLookClick(item: Look) {

    }

    private fun handleLooks(items: List<Look>) {
        looksAdapter.replaceAll(items)
    }

    override fun initView() {
        initRecycler()
        viewModel.fetchItems()
    }

    private fun initRecycler() {
        binding.looksRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = looksAdapter
            looksAdapter.setOnItemClick { _, _ -> }
        }
    }

}