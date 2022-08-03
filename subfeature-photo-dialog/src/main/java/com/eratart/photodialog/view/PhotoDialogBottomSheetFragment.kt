package com.eratart.photodialog.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.alert.AlertUtil.showOkAlert
import com.eratart.baseui.base.fragment.BaseBottomSheetDialogFragment
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.getScreenWidth
import com.eratart.baseui.extensions.setGoneIfNull
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.withArgs
import com.eratart.photodialog.R
import com.eratart.photodialog.databinding.FragmentPhotoDialogBinding
import com.eratart.photodialog.viewmodel.PhotoDialogViewModel
import com.eratart.tools.gallery.choose.GalleryController
import com.eratart.tools.gallery.choose.GalleryControllerCallbacks
import com.eratart.tools.gallery.saver.IGallerySaver
import com.eratart.tools.permissions.IPermissionsManager
import com.eratart.tools.permissions.PermissionsManager
import com.eratart.tools.photo.TakePhotoCallbacks
import com.eratart.tools.photo.TakePhotoController
import com.eratart.tools.photo.TakePhotoError
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.eratart.baseui.R as baseUiR

class PhotoDialogBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentPhotoDialogBinding>(R.layout.fragment_photo_dialog) {

    companion object {
        private const val TAG = "PhotoDialogFragment"

        private const val ARGS_TITLE_KEY = "PhotoDialogFragment.ARGS_TITLE_KEY"
        private const val ARGS_DESCRIPTION_KEY = "PhotoDialogFragment.ARGS_DESCRIPTION_KEY"
        private const val ARGS_IS_GUIDE_VISIBLE = "PhotoDialogFragment.ARGS_IS_GUIDE_VISIBLE"
        private val ITEM_BUTTON_MARGIN = 8f.dpToPx()
    }

    override val fragmentTag = TAG
    override val viewModel: PhotoDialogViewModel by viewModel()

    override val binding: FragmentPhotoDialogBinding by viewBinding()
    private val gallerySaver: IGallerySaver by inject()

    private var title: String? = null
    private var description: String? = null
    private var isGuideVisible = false
    private var photoResultListener: PhotoDialogResultListener? = null
    private val permissionManager: IPermissionsManager by lazy {
        PermissionsManager(requireActivity())
    }

    private val takePhotoCallbacks = object : TakePhotoCallbacks {
        override fun onPhotoTaken(uri: Uri) {
            photoResultListener?.photoResultListener(uri)
            dismiss()
        }

        override fun onCancelled() {}
        override fun onFailed(error: TakePhotoError) {
            showOkAlert(
                context = requireContext(),
                titleRes = baseUiR.string.failure_general,
                descriptionRes = error.message,
            )
        }
    }

    private val galleryControllerCallbacks = object : GalleryControllerCallbacks {
        override fun onPhotoTaken(uri: Uri) {
            photoResultListener?.photoResultListener(uri)
            dismiss()
        }

        override fun onFailure() {}
    }

    private val takePhotoController = TakePhotoController(
        fragment = this,
        callbacks = takePhotoCallbacks,
        gallerySaver = gallerySaver
    )

    private val galleryController = GalleryController(
        fragment = this,
        callbacks = galleryControllerCallbacks
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            title = getString(ARGS_TITLE_KEY)
            description = getString(ARGS_DESCRIPTION_KEY)
            isGuideVisible = getBoolean(ARGS_IS_GUIDE_VISIBLE)
        }
    }

    override fun initView() {
        val itemButtonHeight = (requireContext().getScreenWidth() / 2f) - ITEM_BUTTON_MARGIN
        with(binding) {
            tvTitle.setGoneIfNull(title)
            tvDescription.setGoneIfNull(description)
            tvGuide.isVisible = isGuideVisible

            ibTakePhoto.setHeight(itemButtonHeight)
            ibTakePhoto.setOnClickListener {
                takePhotoController.takePhoto()
            }

            ibFromGallery.setHeight(itemButtonHeight)
            ibFromGallery.setOnClickListener {
                if (permissionManager.checkReadExternalStoragePermission()) {
                    galleryController.openGallery()
                }
            }
        }
    }

    class Builder(val context: Context) {

        private var title: String? = null
        private var description: String? = null
        private var isGuideVisible = false
        private var photoResultListener: PhotoDialogResultListener? = null

        fun title(@StringRes resId: Int) = this.apply {
            title = context.getString(resId)
        }

        fun description(@StringRes resId: Int) = this.apply {
            description = context.getString(resId)
        }

        fun isGuideVisible(isVisible: Boolean) = this.apply {
            isGuideVisible = isVisible
        }

        fun photoResultListener(listener: PhotoDialogResultListener) = this.apply {
            photoResultListener = listener
        }

        fun build(): PhotoDialogBottomSheetFragment {
            val fragment = PhotoDialogBottomSheetFragment().withArgs {
                title?.let { putString(ARGS_TITLE_KEY, it) }
                description?.let { putString(ARGS_DESCRIPTION_KEY, it) }
                putBoolean(ARGS_IS_GUIDE_VISIBLE, this@Builder.isGuideVisible)
            }
            fragment.photoResultListener = photoResultListener
            return fragment
        }
    }
}
