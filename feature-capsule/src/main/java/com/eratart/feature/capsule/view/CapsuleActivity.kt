package com.eratart.feature.capsule.view

import android.app.Activity
import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.view.viewpager.setupWithHackTL
import com.eratart.baseui.view.viewpager.setupWithNestedScroll
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.eratart.domain.model.domain.Capsule
import com.eratart.feature.capsule.R
import com.eratart.feature.capsule.databinding.ActivityCapsuleBinding
import com.eratart.feature.capsule.di.CapsuleModule
import com.eratart.subfeature.looks.di.LooksModule
import com.eratart.feature.capsule.viewmodel.CapsuleViewModel
import com.eratart.subfeature.items.di.ItemsModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class CapsuleActivity :
    BaseActivity<CapsuleViewModel, ActivityCapsuleBinding>(R.layout.activity_capsule) {

    companion object {
        private const val EXTRA_CAPSULE = "CapsuleActivity.EXTRA_CAPSULE"

        fun newInstance(activity: Activity, capsule: Capsule): Intent {
            return Intent(activity, CapsuleActivity::class.java).apply {
                putExtra(EXTRA_CAPSULE, capsule)
            }
        }
    }

    private val capsule by lazy { intent.getParcelableExtra<Capsule>(EXTRA_CAPSULE) }

    override val koinModules = listOf(CapsuleModule,
        ItemsModule, LooksModule
    )
    override val viewModel: CapsuleViewModel by viewModel()

    private var pagerAdapter: CapsuleViewPagerAdapter? = null

    override val binding: ActivityCapsuleBinding by viewBinding()
    private val vpCapsule by lazy { binding.vpCapsule }
    private val tlCapsule by lazy { binding.tlCapsule }
    private val tlCapsule2 by lazy { binding.tlCapsule2 }
    private val nestedScroll by lazy { binding.nestedScroll }
    private val clHeader by lazy { binding.clHeader }
    private val ivCapsule by lazy { binding.ivCapsule }
    private val tvCapsuleDescription by lazy { binding.tvCapsuleDescription }
    private val tvCapsuleTitle by lazy { binding.tvCapsuleTitle }
    private val btnMore by lazy { binding.btnMore }
    private val clHackTL by lazy { binding.clHackTL }
    private val btnBack by lazy { binding.btnBack }
    private val viewHeaderBg by lazy { binding.viewHeaderBg }
    private val clNavHeader by lazy { binding.clNavHeader }

    private fun getNames() = arrayListOf(
        getString(com.eratart.subfeature.items.R.string.subfeature_items_tab_items),
        getString(R.string.feature_capsule_tab_looks),
    )

    override fun initView() {
        capsule?.let { initCapsule(it) }
        btnBack.setOnClickListener { onBackPressed() }
    }

    private fun initCapsule(capsule: Capsule) {
        capsule.apply {
            ivCapsule.loadImageWithGlide(mainPhoto)
            tvCapsuleTitle.text = title
            tvCapsuleDescription.text = description
        }
        ivCapsule.setHeight(getScreenWidth() * FloatConstants.ASPECT_RATIO_4_5)
        btnMore.setOnClickListener {}
    }

    override fun onLayoutReady() {
        addStatusBarMargin(clNavHeader)
        clNavHeader.setOnClickListener {  }
        val scrollLimit = clHeader.height - clNavHeader.height - getStatusBarHeight()
        val emptyItemHeight = getScreenHeight() - clHeader.height - tlCapsule.height
        pagerAdapter = CapsuleViewPagerAdapter(this, emptyItemHeight)
        vpCapsule.apply {
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter?.itemCount ?: IntConstants.ZERO
            isUserInputEnabled = false
            setupWithNestedScroll(nestedScroll, scrollLimit)
            setupWithHackTL(
                tlCapsule, tlCapsule2, getNames(), nestedScroll, scrollLimit, clHackTL
            ) {
                viewHeaderBg.alpha = it
            }
        }
    }

    override fun initViewModel() {

    }
}