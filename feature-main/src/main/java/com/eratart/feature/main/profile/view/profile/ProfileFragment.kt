package com.eratart.feature.main.profile.view.profile

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.baseui.extensions.getScreenHeight
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.loadImageWithGlideBlur
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.view.viewpager.setupWithHackTL
import com.eratart.baseui.view.viewpager.setupWithNestedScroll
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.domain.Item
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.FragmentProfileBinding
import com.eratart.feature.main.profile.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment :
    BaseFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {

    companion object {

        private const val COUNT_DEFAULT_VALUE = "-"
        private const val MAX_WARDROBE_MARK = 5f

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override val binding: FragmentProfileBinding by viewBinding()
    override val viewModel: ProfileViewModel by viewModel()
    private var pagerAdapter: ProfileViewPagerAdapter? = null

    private fun getNames() = arrayListOf(
        getString(com.eratart.subfeature.items.R.string.subfeature_items_tab_items),
        getString(R.string.feature_main_profile_tab_capsules),
    )

    override fun onResume() {
        super.onResume()
        viewModel.fetchUser()
    }

    override fun initFragment() {
        viewModel.apply {
            registerObserver(droppUser, ::handleDroppUser)
            registerObserver(items, ::handleItems)
            registerObserver(capsules, ::handleCapsules)
            registerObserver(wardrobeMark, ::handleWardrobeMark)
        }
    }

    private fun handleItems(items: List<Item>) {
        val count = items.size
        binding.tvItemsCount.text = count.toString()
    }

    private fun handleCapsules(capsules: List<Capsule>) {
        val count = capsules.size
        binding.tvCapsulesCount.text = count.toString()
    }

    private fun handleWardrobeMark(mark: Float) {
        val text = if (mark == FloatConstants.MINUS_ONE) {
            COUNT_DEFAULT_VALUE
        } else {
            (mark * MAX_WARDROBE_MARK).toString()
        }
        binding.tvMarkWardrobe.text = text
    }

    private fun handleDroppUser(droppUser: DroppUser) {
        droppUser.apply {
            binding.apply {
                tvNickname.text = "@$username"
                tvBio.text = description
                tvName.text = firstName.plus(StringConstants.SPACE).plus(lastName)
                photo?.apply {
                    ivAvatar.loadImageWithGlide(this)
                    ivBlurBg.loadImageWithGlideBlur(this)
                }
            }
        }
    }

    override fun initView() {
        binding.apply {
            clNavHeader.apply {
                setOnClickListener {}
            }
            btnChat.setOnClickListener {
                navigator.startDialogsListActivity(requireActivity())
            }
            btnSettings.setOnClickListener {
                navigator.startSettingsActivity(requireActivity())
            }
            clWardrobe.setOnClickListener {}
        }

        viewModel.fetchUser()
    }

    override fun onLayoutReady() {
        binding.apply {
            clNavHeader.apply {
                activity?.addStatusBarMargin(this)
            }
            val scrollLimit =
                viewProfileHeader.height -
                        clNavHeader.height -
                        requireActivity().getStatusBarHeight()
            val emptyItemHeight =
                requireActivity().getScreenHeight() -
                        viewProfileHeader.height -
                        tlProfile.height -
                        resources.getDimension(R.dimen.main_navbar_height).toInt()
            pagerAdapter = ProfileViewPagerAdapter(requireActivity(), emptyItemHeight)

            vpProfile.apply {
                adapter = pagerAdapter
                offscreenPageLimit = pagerAdapter?.itemCount ?: IntConstants.ZERO
                isUserInputEnabled = false


                setupWithNestedScroll(nestedScroll, scrollLimit)
                setupWithHackTL(
                    tlProfile, tlProfile2, getNames(), nestedScroll, scrollLimit, clHackTl
                ) {
                    viewHeaderBg.alpha = it
                }
            }
        }
    }
}