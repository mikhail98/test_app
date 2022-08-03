package com.eratart.feature.item.creation.view.fit

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.setHeight
import com.eratart.domain.model.enums.Fit
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.BottomSheetFitFragmentBinding
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FitBottomSheetFragment :
    BaseBottomSheetDialogFragment<BottomSheetFitFragmentBinding>(R.layout.bottom_sheet_fit_fragment) {

    companion object {
        private const val TAG = "FitBottomSheetFragment¬"
        private val ITEM_BUTTON_MARGIN = 8f.dpToPx()

        fun newInstance() = FitBottomSheetFragment()
    }

    override val fragmentTag = TAG
    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding by viewBinding<BottomSheetFitFragmentBinding>()

    override fun initView() {
        with(binding) {
            context?.apply {
                val height = (getScreenWidth() / 2f) - ITEM_BUTTON_MARGIN
                ibS.setHeight(height)
                ibM.setHeight(height)
                ibL.setHeight(height)
                ibXL.setHeight(height)
            }

            ibS.setOnClickListener { selectFit(Fit.SLIM) }
            ibM.setOnClickListener { selectFit(Fit.REGULAR) }
            ibL.setOnClickListener { selectFit(Fit.COMFORT) }
            ibXL.setOnClickListener { selectFit(Fit.OVERSIZE) }
        }
    }

    private fun selectFit(fit: Fit) {
        viewModel.selectFit(fit)
        dismiss()
    }
}