package com.eratart.feature.main.mainactivity.view

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.feature.main.feed.view.feed.FeedFragment
import com.eratart.feature.main.profile.view.profile.ProfileFragment

class MainViewPagerAdapter(activity: FragmentActivity) : BaseViewPager2Adapter(activity) {

    override val fragments by lazy {
        arrayListOf(FeedFragment.newInstance(), ProfileFragment.newInstance())
    }
}