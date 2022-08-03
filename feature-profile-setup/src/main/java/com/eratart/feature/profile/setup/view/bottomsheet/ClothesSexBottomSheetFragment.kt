package com.eratart.feature.profile.setup.view.bottomsheet

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.setHeight
import com.eratart.domain.model.enums.Gender
import com.eratart.feature.profile.setup.R
import com.eratart.feature.profile.setup.databinding.FragmentBottomSheetSexBinding
import com.eratart.feature.profile.setup.viewmodel.SetupProfileVewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClothesSexBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentBottomSheetSexBinding>(R.layout.fragment_bottom_sheet_sex) {

    companion object {
        private const val TAG = "ClothesSexBottomSheetFragment"
        private val ITEM_BUTTON_MARGIN = 8f.dpToPx()

        fun newInstance() = ClothesSexBottomSheetFragment()
    }

    override val fragmentTag = TAG
    override val viewModel: SetupProfileVewModel by viewModel()
    override val binding by viewBinding<FragmentBottomSheetSexBinding>()

    override fun initView() {
        with(binding) {
            context?.apply {
                val height = (getScreenWidth() / 2f) - ITEM_BUTTON_MARGIN
                ibMan.setHeight(height)
                ibWoman.setHeight(height)
                ibUnisex.setHeight(height)
            }

            ibMan.setOnClickListener { selectSex(Gender.MAN) }
            ibWoman.setOnClickListener { selectSex(Gender.WOMAN) }
            ibUnisex.setOnClickListener { selectSex(Gender.UNISEX) }
        }
    }

    private fun selectSex(sex: Gender) {
        viewModel.setSex(sex)
        dismiss()
    }
}