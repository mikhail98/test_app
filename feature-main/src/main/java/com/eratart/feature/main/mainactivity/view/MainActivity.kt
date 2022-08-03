package com.eratart.feature.main.mainactivity.view

import android.app.Activity
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.view.viewpager.addChangeListener
import com.eratart.baseui.view.viewpager.addDebounceChangeListener
import com.eratart.core.constants.IntConstants
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.ActivityMainBinding
import com.eratart.feature.main.feed.di.FeedModule
import com.eratart.feature.main.feed.view.adverts.di.AdvertsModule
import com.eratart.feature.main.mainactivity.di.MainModule
import com.eratart.feature.main.mainactivity.viewmodel.MainViewModel
import com.eratart.feature.main.profile.di.ProfileModule
import com.eratart.feature.main.profile.view.capsules.di.CapsulesModule
import com.eratart.subfeature.items.di.ItemsModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, MainActivity::class.java)
        }
    }

    override val koinModules = listOf(
        MainModule,
        FeedModule,
        AdvertsModule,
        ProfileModule,
        CapsulesModule, ItemsModule
    )
    override val viewModel: MainViewModel by viewModel()

    private val pagerAdapter by lazy { MainViewPagerAdapter(this) }

    override val binding: ActivityMainBinding by viewBinding()
    private val vpMain by lazy { binding.vpMain }
    private val btnFeed by lazy { binding.btnFeed }
    private val btnAddItem by lazy { binding.btnAddItem }
    private val btnProfile by lazy { binding.btnProfile }

    override fun initView() {
        initClickListeners()
        initViewPager()
    }

    private fun initClickListeners() {
        btnAddItem.setOnClickListener { navigator.startItemCreationActivity(this) }
        btnFeed.setOnClickListener { vpMain.currentItem = IntConstants.ZERO }
        btnProfile.setOnClickListener { vpMain.currentItem = IntConstants.ONE }
    }

    private fun initViewPager() {
        vpMain.isUserInputEnabled = false
        vpMain.adapter = pagerAdapter
        vpMain.addDebounceChangeListener { }
        vpMain.addChangeListener { position -> updateCurrentPageIcon(position) }
        vpMain.currentItem = IntConstants.ZERO
    }

    private fun updateCurrentPageIcon(position: Int) {
        if (position == IntConstants.ZERO) {
            btnFeed.loadImageWithGlide(R.drawable.ic_feed_selected)
            btnProfile.loadImageWithGlide(R.drawable.ic_profile)
        } else {
            btnFeed.loadImageWithGlide(R.drawable.ic_feed)
            btnProfile.loadImageWithGlide(R.drawable.ic_profile_selected)
        }
    }

    override fun initViewModel() {
    }
}