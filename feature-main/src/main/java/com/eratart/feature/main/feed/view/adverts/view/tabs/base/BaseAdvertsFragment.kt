package com.eratart.feature.main.feed.view.adverts.view.tabs.base

import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.core.constants.FloatConstants
import com.eratart.domain.model.domain.Advert
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.FragmentAdvertsBinding
import com.eratart.feature.main.feed.view.adverts.entity.AdvertTab
import com.eratart.feature.main.feed.view.adverts.view.adapter.AdvertsAdapter
import com.eratart.feature.main.feed.view.adverts.viewmodel.AdvertsViewModel
import com.eratart.subfeature.chips.api.ChipItem
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseAdvertsFragment :
    BaseFragment<AdvertsViewModel, FragmentAdvertsBinding>(R.layout.fragment_adverts),
    AdvertsAdapter.IListener {

    companion object {
        private const val ITEMS_IN_ROW = 2
    }

    abstract val tab: AdvertTab
    abstract val shopChips: Boolean

    abstract val emptyTitleRes: Int
    abstract val emptyDescriptionRes: Int
    abstract val emptyIconRes: Int

    override val binding: FragmentAdvertsBinding by viewBinding()
    override val viewModel: AdvertsViewModel by viewModel()

    private var isFirstInit = true
    private val initList = ArrayList<Advert>()

    private val paddingOffset by lazy { 20f.dpToPx() }
    private val advertsCardHeight by lazy {
        (requireContext().getScreenWidth() / 2f - paddingOffset) / FloatConstants.ASPECT_RATIO_3_4
    }
    private val advertsAdapter by lazy {
        AdvertsAdapter(mutableListOf(), advertsCardHeight, this)
    }

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

    override fun initView() {
        binding.apply {
            emptyView.setTitle(emptyTitleRes)
            emptyView.setDescription(emptyDescriptionRes)
            emptyView.setIcon(emptyIconRes)

            clEmpty.setOnClickListener { }

            rvAdverts.apply {
                layoutManager = GridLayoutManager(requireContext(), ITEMS_IN_ROW)
                adapter = advertsAdapter
                advertsAdapter.setOnItemClick { item, _ ->
                    navigator.startItemActivity(requireActivity(), item.item, item)
                }
            }
            rvChips.apply {
                isVisible = shopChips
                layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = chipsAdapter
                chipsAdapter.setOnItemClick { chipItem, pos ->
                    val newItem = chipItem.copy(isSelected = !chipItem.isSelected)
                    chipsAdapter.replaceItem(newItem, pos)
                }
            }
        }
    }

    override fun onFavoriteClick(item: Advert, newState: Boolean) {
        viewModel.handleFavorites(tab, item, newState)
    }

    override fun onMoreAdvertClick(item: Advert) {

    }

    override fun initFragment() {
        viewModel.apply {
            registerObserver(adverts, ::handleAdverts)
        }
        viewModel.fetchAdverts(tab)
    }

    private fun handleAdverts(advertsList: List<Advert>) {
        if (isFirstInit) {
            initList.replaceAllWith(advertsList)
            isFirstInit = false
        }
        advertsAdapter.replaceAll(advertsList)
        binding.clEmpty.apply {
            if (advertsList.isEmpty()) {
                visibleWithAlpha()
            } else {
                gone()
            }
        }
    }
}