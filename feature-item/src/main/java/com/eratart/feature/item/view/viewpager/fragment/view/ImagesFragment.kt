package com.eratart.feature.item.view.viewpager.fragment.view

import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.fragment.BaseFragment
import com.eratart.baseui.extensions.lazyArgument
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.withArgs
import com.eratart.feature.item.R
import com.eratart.feature.item.databinding.FragmentImageBinding
import com.eratart.feature.item.view.viewpager.fragment.viewmodel.ImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesFragment : BaseFragment<ImageViewModel, FragmentImageBinding>(R.layout.fragment_image) {

    companion object {
        private const val ARGS_URL = "ImagesFragment.ARGS_URL"

        fun newInstance(photoUrl: String): ImagesFragment {
            return ImagesFragment().withArgs {
                putString(ARGS_URL, photoUrl)
            }
        }
    }

    override val viewModel: ImageViewModel by viewModel()
    override val binding: FragmentImageBinding by viewBinding()
    private val photoUrl by lazyArgument<String>(ARGS_URL)

    override fun initFragment() {

    }

    override fun initView() {
        binding.ivImage.loadImageWithGlide(photoUrl)
    }

}

