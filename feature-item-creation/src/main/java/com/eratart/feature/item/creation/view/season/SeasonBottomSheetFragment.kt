package com.eratart.feature.item.creation.view.season

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.setHeight
import com.eratart.domain.model.enums.Season
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetSeasonFragmentBinding
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetSeasonFragmentBinding>(R.layout.bottom_sheet_season_fragment) {

    companion object {
        private const val TAG = "SeasonBottomSheetFragment"
        private val ITEM_BUTTON_MARGIN = 8f.dpToPx()

        fun newInstance() = SeasonBottomSheetFragment()
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetSeasonFragmentBinding>()

    override fun initView() {
        with(binding) {
            context?.apply {
                val height = (getScreenWidth() / 2f) - ITEM_BUTTON_MARGIN
                ibSummer.setHeight(height)
                ibWinter.setHeight(height)
                ibDemieason.setHeight(height)
            }

            ibSummer.setOnClickListener { selectSeason(Season.SUMMER) }
            ibWinter.setOnClickListener { selectSeason(Season.WINTER) }
            ibDemieason.setOnClickListener { selectSeason(Season.DEMISEASON) }
        }
    }

    private fun selectSeason(season: Season) {
        viewModel.selectSeason(season)
        dismiss()
    }
}