package com.eratart.feature.main.feed.view.feed

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.baseui.view.viewpager.setUpWithTabLayout
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.FragmentFeedBinding
import com.eratart.feature.main.feed.viewmodel.FeedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : BaseFragment<FeedViewModel, FragmentFeedBinding>(R.layout.fragment_feed) {

    companion object {
        fun newInstance(): FeedFragment {
            return FeedFragment()
        }
    }

    override val binding: FragmentFeedBinding by viewBinding()
    override val viewModel: FeedViewModel by viewModel()

    private val pagerAdapter by lazy { FeedViewPagerAdapter(requireActivity()) }

    private fun getNames() = arrayListOf(
        getString(R.string.feature_main_feed_feed),
        getString(R.string.feature_main_feed_favorite),
        getString(R.string.feature_main_feed_my_items),
    )

    override fun initFragment() {

    }

    override fun initView() {
        binding.apply {
            vpFeed.apply {
                adapter = pagerAdapter
                offscreenPageLimit = pagerAdapter.itemCount
                isUserInputEnabled = false
                setUpWithTabLayout(tlFeed.tabLayout, getNames())
            }
        }
    }

    override fun onLayoutReady() {
        super.onLayoutReady()
        requireActivity().addStatusBarMargin(binding.tlFeed)
    }
}