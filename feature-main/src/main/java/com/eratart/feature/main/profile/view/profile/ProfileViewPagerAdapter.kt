package com.eratart.feature.main.profile.view.profile

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.feature.main.profile.view.capsules.view.CapsulesFragment
import com.eratart.subfeature.items.view.ItemsFragment

class ProfileViewPagerAdapter(activity: FragmentActivity, emptyItemHeight: Int) :
    BaseViewPager2Adapter(activity) {

    override val fragments by lazy {
        arrayListOf(
            ItemsFragment.newInstance(emptyItemHeight, false),
            CapsulesFragment.newInstance(emptyItemHeight)
        )
    }
}