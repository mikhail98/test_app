package com.eratart.feature.profile.setup.view.bottomsheet

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.setHeight
import com.eratart.domain.model.enums.Extravagance
import com.eratart.feature.profile.setup.R
import com.eratart.feature.profile.setup.databinding.FragmentBottomSheetTypeBinding
import com.eratart.feature.profile.setup.viewmodel.SetupProfileVewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClothesTypeBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentBottomSheetTypeBinding>(R.layout.fragment_bottom_sheet_type) {

    companion object {
        private const val TAG = "ClothesTypeBottomSheetFragment"
        private val ITEM_BUTTON_MARGIN = 8f.dpToPx()

        fun newInstance() = ClothesTypeBottomSheetFragment()
    }

    override val fragmentTag = TAG
    override val viewModel: SetupProfileVewModel by viewModel()

    override val binding by viewBinding<FragmentBottomSheetTypeBinding>()

    override fun initView() {
        with(binding) {
            context?.apply {
                val height = (getScreenWidth() / 2f) - ITEM_BUTTON_MARGIN
                ibLow.setHeight(height)
                ibMedium.setHeight(height)
                ibHigh.setHeight(height)
                ibElton.setHeight(height)
            }

            ibLow.setOnClickListener { selectExtravagance(Extravagance.LOW) }
            ibMedium.setOnClickListener { selectExtravagance(Extravagance.MEDIUM) }
            ibHigh.setOnClickListener { selectExtravagance(Extravagance.HIGH) }
            ibElton.setOnClickListener { selectExtravagance(Extravagance.ELTON_JOHN) }
        }
    }

    private fun selectExtravagance(extravagance: Extravagance) {
        viewModel.setExtravagance(extravagance)
        dismiss()
    }
}