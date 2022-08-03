package com.eratart.feature.capsule.view

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.subfeature.items.view.ItemsFragment
import com.eratart.subfeature.looks.view.LooksFragment

class CapsuleViewPagerAdapter(activity: FragmentActivity, emptyItemHeight: Int) :
    BaseViewPager2Adapter(activity) {

    override val fragments by lazy {
        arrayListOf(ItemsFragment.newInstance(emptyItemHeight, true), LooksFragment.newInstance())
    }
}