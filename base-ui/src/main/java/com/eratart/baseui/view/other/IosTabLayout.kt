package com.eratart.baseui.view.other

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.eratart.baseui.R
import com.eratart.baseui.extensions.initTabLayout
import com.google.android.material.tabs.TabLayout

class IosTabLayout(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_ios_tab_layout, this)

    val tabLayout: TabLayout by lazy { view.findViewById(R.id.tabLayout) }

    fun initTabLayout(names: List<String>, onTabChangedListener: (Int) -> Unit){
        tabLayout.initTabLayout(names, onTabChangedListener)
    }
}