package com.eratart.feature.main.profile.view.capsules.view

import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.visible
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.baseui.extensions.withArgs
import com.eratart.core.constants.FloatConstants.ASPECT_RATIO_3_4
import com.eratart.core.constants.IntConstants
import com.eratart.domain.model.domain.Capsule
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.FragmentCapsulesBinding
import com.eratart.feature.main.profile.view.capsules.view.adapter.CapsulesAdapter
import com.eratart.feature.main.profile.view.capsules.viewmodel.CapsulesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CapsulesFragment :
    BaseFragment<CapsulesViewModel, FragmentCapsulesBinding>(R.layout.fragment_capsules),
    CapsulesAdapter.IListener {

    companion object {
        private const val ITEMS_IN_ROW = 2

        private const val ARGS_EMPTY_ITEM_HEIGHT = "CapsulesFragment.ARGS_EMPTY_ITEM_HEIGHT"

        fun newInstance(emptyItemHeight: Int): CapsulesFragment {
            return CapsulesFragment().withArgs {
                putInt(ARGS_EMPTY_ITEM_HEIGHT, emptyItemHeight)
            }
        }
    }

    override val binding: FragmentCapsulesBinding by viewBinding()
    override val viewModel: CapsulesViewModel by viewModel()

    private val paddingOffset by lazy { 20f.dpToPx() }
    private val capsuleCardHeight by lazy {
        (requireContext().getScreenWidth() / 2f - paddingOffset) / ASPECT_RATIO_3_4
    }
    private val capsulesAdapter by lazy {
        CapsulesAdapter(mutableListOf(), capsuleCardHeight, this)
    }
    private val emptyItemHeight by lazyArgument(ARGS_EMPTY_ITEM_HEIGHT, IntConstants.ZERO)

    override fun initFragment() {
        viewModel.apply {
            registerObserver(capsules, ::handleCapsules)
        }
    }

    private fun handleCapsules(items: List<Capsule>) {
        capsulesAdapter.replaceAll(items)
        binding.apply {
            if (items.isEmpty()) {
                capsulesRecycler.gone()
                layoutEmpty.root.visibleWithAlpha()
            } else {
                capsulesRecycler.visible()
                layoutEmpty.root.gone()
            }
        }
    }

    override fun initView() {
        initRecycler()
        binding.layoutEmpty.apply {
            root.setHeight(emptyItemHeight)
            clEmptyProfile.setOnClickListener {
                navigator.startItemCreationActivity(requireActivity())
            }
        }
        viewModel.fetchCapsules()
    }

    private fun initRecycler() {
        binding.capsulesRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), ITEMS_IN_ROW)
            adapter = capsulesAdapter
            capsulesAdapter.setOnItemClick { item, _ ->
                navigator.startCapsuleActivity(requireActivity(), item)
            }
        }
    }

    override fun onMoreCapsuleClick(item: Capsule) {

    }
}