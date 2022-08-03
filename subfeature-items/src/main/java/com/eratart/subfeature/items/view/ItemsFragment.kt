package com.eratart.subfeature.items.view

import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.eratart.domain.model.domain.Item
import com.eratart.subfeature.chips.api.ChipItem
import com.eratart.subfeature.items.databinding.FragmentItemsBinding
import com.eratart.subfeature.items.view.adapter.ItemsAdapter
import com.eratart.subfeature.items.view.chips.ChipAdapter
import com.eratart.subfeature.items.viewmodel.ItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemsFragment :
    BaseFragment<ItemsViewModel, FragmentItemsBinding>(com.eratart.subfeature.items.R.layout.fragment_items),
    ItemsAdapter.IListener {

    companion object {
        private const val ITEMS_IN_ROW = 2

        private const val ARGS_EMPTY_ITEM_HEIGHT = "ItemsFragment.ARGS_EMPTY_ITEM_HEIGHT"
        private const val ARGS_SHOW_CHIPS = "ItemsFragment.ARGS_SHOW_CHIPS"

        fun newInstance(emptyItemHeight: Int, showChips: Boolean): ItemsFragment {
            return ItemsFragment().withArgs {
                putInt(ARGS_EMPTY_ITEM_HEIGHT, emptyItemHeight)
                putBoolean(ARGS_SHOW_CHIPS, showChips)
            }
        }
    }

    override val binding: FragmentItemsBinding by viewBinding()
    override val viewModel: ItemsViewModel by viewModel()
    private val itemCardHeightMargin by lazy { 20f.dpToPx() }
    private val itemCardHeight by lazy { (requireContext().getScreenWidth() / 2f - itemCardHeightMargin) / ASPECT_RATIO_3_4 }
    private val itemsAdapter by lazy { ItemsAdapter(mutableListOf(), itemCardHeight, this) }
    private val emptyItemHeight by lazyArgument(ARGS_EMPTY_ITEM_HEIGHT, IntConstants.ZERO)
    private val showChips by lazyArgument(ARGS_SHOW_CHIPS, false)

    private val chipsList: MutableList<ChipItem<String>> by lazy {
        arrayListOf(
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", ""),
            ChipItem(false, "Jeans, skirts", "")
        )
    }
    private val chipsAdapter by lazy { ChipAdapter(chipsList) }

    override fun initFragment() {
        viewModel.apply {
            registerObserver(items, ::handleItems)
        }
    }

    override fun onMoreItemClick(item: Item) {

    }

    private fun handleItems(items: List<Item>) {
        itemsAdapter.replaceAll(items)
        binding.apply {
            if (items.isEmpty()) {
                rvItems.gone()
                rvChips.gone()
                layoutEmpty.root.visibleWithAlpha()
            } else {
                rvChips.isVisible = showChips
                rvItems.visible()
                layoutEmpty.root.gone()
            }
        }
    }

    override fun initView() {
        binding.apply {
            layoutEmpty.apply {
                root.setHeight(emptyItemHeight)
                clEmptyProfile.setOnClickListener {
                    navigator.startItemCreationActivity(requireActivity())
                }
            }
            rvItems.apply {
                layoutManager = GridLayoutManager(requireContext(), ITEMS_IN_ROW)
                adapter = itemsAdapter
                itemsAdapter.setOnItemClick { item, _ ->
                    navigator.startItemActivity(requireActivity(), item, null)
                }
            }
            rvChips.apply {
                isVisible = showChips
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = chipsAdapter
                chipsAdapter.setOnItemClick { chipItem, pos ->
                    val newItem = chipItem.copy(isSelected = !chipItem.isSelected)
                    chipsAdapter.replaceItem(newItem, pos)
                }
            }
        }
        viewModel.fetchItems()
    }

}