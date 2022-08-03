package com.eratart.feature.item.view

import android.app.Activity
import android.content.Intent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.setViewPageScroller
import com.eratart.baseui.view.viewpager.ViewPageScroller
import com.eratart.baseui.view.viewpager.addScrollLimitListener
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Item
import com.eratart.domain.model.domain.Look
import com.eratart.domain.model.enums.Season
import com.eratart.feature.item.R
import com.eratart.feature.item.databinding.ActivityItemBinding
import com.eratart.feature.item.di.ItemModule
import com.eratart.feature.item.view.viewpager.ImagesViewPagerAdapter
import com.eratart.feature.item.view.viewpager.fragment.di.ImageModule
import com.eratart.feature.item.viewmodel.ItemViewModel
import com.eratart.subfeature.looks.view.adapter.LooksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemActivity : BaseActivity<ItemViewModel, ActivityItemBinding>(R.layout.activity_item),
    LooksAdapter.IListener {

    companion object {
        private const val EXTRAS_ITEM = "ItemActivity.EXTRAS_ITEM"
        private const val EXTRAS_ADVERT = "ItemActivity.EXTRAS_ADVERT"

        fun newInstance(activity: Activity, item: Item, advert: Advert?): Intent {
            return Intent(activity, ItemActivity::class.java).apply {
                putExtra(EXTRAS_ITEM, item)
                putExtra(EXTRAS_ADVERT, advert)
            }
        }
    }

    private val item by lazy { intent.getParcelableExtra<Item>(EXTRAS_ITEM) }
    private val advert by lazy { intent.getParcelableExtra<Advert>(EXTRAS_ADVERT) }

    override val koinModules = listOf(ItemModule, ImageModule)

    override val viewModel: ItemViewModel by viewModel()
    override val binding: ActivityItemBinding by viewBinding()
    private val vpImages by lazy { binding.vpImages }
    private val tlDots by lazy { binding.tlDots }
    private val tvItemTitle by lazy { binding.tvItemTitle }
    private val tvItemCategory by lazy { binding.tvItemCategory }
    private val btnMore by lazy { binding.btnMore }

    private val tvSize by lazy { binding.tvSize }
    private val tvBrand by lazy { binding.tvBrand }
    private val tvSeason by lazy { binding.tvSeason }
    private val btnCapsule by lazy { binding.btnCapsule }
    private val tvCapsulesCount by lazy { binding.tvCapsulesCount }
    private val rvLooks by lazy { binding.rvLooks }
    private val btnCreateAdvert by lazy { binding.btnCreateAdvert }

    private val viewHeaderBg by lazy { binding.viewHeaderBg }
    private val clNavHeader by lazy { binding.clNavHeader }
    private val btnBack by lazy { binding.btnBack }
    private val nestedScroll by lazy { binding.nestedScroll }

    private val photosList by lazy { (item?.photos ?: listOf()).toMutableList() }
    private val imagesVpAdapter by lazy { ImagesViewPagerAdapter(this, photosList) }

    private val itemCardWidthMargin by lazy { 86f.dpToPx() }
    private val itemCardWidth by lazy { getScreenWidth() - itemCardWidthMargin }
    private val itemCardHeight by lazy { itemCardWidth * FloatConstants.ASPECT_RATIO_4_5 }
    private val looksList by lazy { (item?.looks ?: listOf()).toMutableList() }
    private val looksAdapter by lazy {
        LooksAdapter(looksList, itemCardHeight, itemCardWidth, this)
    }

    override fun initView() {
        item?.apply {
            initItem(this)
        }
    }

    private fun initItem(item: Item) {
        vpImages.apply {
            setHeight(getScreenWidth() / FloatConstants.ASPECT_RATIO_4_5)
            offscreenPageLimit = imagesVpAdapter.itemCount
            adapter = imagesVpAdapter
            tlDots.setViewPager2(this)
            tlDots.isVisible = photosList.size > IntConstants.ONE
            setViewPageScroller(ViewPageScroller(context, AccelerateDecelerateInterpolator()))
        }

        tvSize.text = item.size.size.plus(StringConstants.SPACE).plus(item.size.sizeType.name)
        tvBrand.text = item.brand
        val textRes = when (item.season) {
            Season.SUMMER -> R.string.feature_item_season_summer
            Season.WINTER -> R.string.feature_item_season_winter
            Season.DEMISEASON -> R.string.feature_item_season_demiseason
        }
        tvSeason.setText(textRes)
        tvCapsulesCount.text = item.capsules.size.toString()
        tvItemTitle.text = item.title
        tvItemCategory.text = item.category

        rvLooks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = looksAdapter
        }

        btnCreateAdvert.setOnClickListener {
            navigator.startAdvertCreationActivity(this@ItemActivity, item)
        }
        btnCapsule.setOnClickListener {}
        btnMore.setOnClickListener {}
        btnBack.setOnClickListener { onBackPressed() }
    }

    override fun onMoreLookClick(item: Look) {

    }

    override fun onLayoutReady() {
        addStatusBarMargin(clNavHeader)
        clNavHeader.setOnClickListener { }
        val scrollLimit = vpImages.height - getStatusBarHeight()
        nestedScroll.addScrollLimitListener(scrollLimit) {
            viewHeaderBg.alpha = it
        }
    }

    override fun initViewModel() {

    }
}