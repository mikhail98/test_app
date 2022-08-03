package com.eratart.feature.item.creation.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.addStatusBarMargin
import com.eratart.baseui.extensions.getStatusBarHeight
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.registerObserver
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.baseui.extensions.setOnTextChangedListener
import com.eratart.baseui.extensions.visible
import com.eratart.baseui.extensions.visibleWithAlpha
import com.eratart.baseui.view.viewpager.addScrollLimitListener
import com.eratart.core.constants.StringConstants
import com.eratart.core.ext.capitalize
import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Category
import com.eratart.domain.model.domain.Colors
import com.eratart.domain.model.domain.Size
import com.eratart.domain.model.domain.Style
import com.eratart.domain.model.enums.Fit
import com.eratart.domain.model.enums.Season
import com.eratart.feature.item.creation.R
import com.eratart.feature.item.creation.databinding.ActivityItemCreationBinding
import com.eratart.feature.item.creation.di.ItemCreationModule
import com.eratart.feature.item.creation.view.brand.BrandBottomSheetFragment
import com.eratart.feature.item.creation.view.categories.CategoriesBottomSheetFragment
import com.eratart.feature.item.creation.view.categories.SubCategoriesBottomSheetFragment
import com.eratart.feature.item.creation.view.fit.FitBottomSheetFragment
import com.eratart.feature.item.creation.view.season.SeasonBottomSheetFragment
import com.eratart.feature.item.creation.view.size.SizeBottomSheetFragment
import com.eratart.feature.item.creation.view.style.StyleBottomSheetFragment
import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import com.eratart.photodialog.di.PhotoDialogModule
import com.eratart.photodialog.view.PhotoDialogBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemCreationActivity :
    BaseActivity<ItemCreationViewModel, ActivityItemCreationBinding>(R.layout.activity_item_creation) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, ItemCreationActivity::class.java)
        }
    }

    override val koinModules = listOf(ItemCreationModule, PhotoDialogModule)

    override val viewModel: ItemCreationViewModel by viewModel()
    override val binding: ActivityItemCreationBinding by viewBinding()

    private val viewHeaderBg by lazy { binding.viewHeaderBg }
    private val nestedScroll by lazy { binding.nestedScroll }
    private val clNavHeader by lazy { binding.clNavHeader }
    private val btnBack by lazy { binding.btnBack }

    private val clPhoto by lazy { binding.clPhoto }
    private val btnAddPhoto by lazy { binding.btnAddPhoto }
    private val clAddedPhoto by lazy { binding.clAddedPhoto }
    private val ivPhoto by lazy { binding.ivPhoto }
    private val btnDeletePhoto by lazy { binding.btnDeletePhoto }

    private val sivCategory by lazy { binding.sivCategory }
    private val sivSubCategory by lazy { binding.sivSubCategory }
    private val sivBrand by lazy { binding.sivBrand }
    private val tivModel by lazy { binding.tivModel }
    private val sivSize by lazy { binding.sivSize }
    private val sivSeason by lazy { binding.sivSeason }
    private val sivFit by lazy { binding.sivFit }
    private val sivStyle by lazy { binding.sivStyle }
    private val sivColor by lazy { binding.sivColor }

    private val categories = ArrayList<Categories>()
    private val brands = ArrayList<Brand>()
    private val styles = ArrayList<Style>()

    private var selectedCategory: Category? = null
    private var selectedSubCategory: Category? = null
    private var selectedBrand: Brand? = null
    private var selectedStyle: Style? = null
    private var selectedSize: Size? = null
    private var selectedColors: Colors? = Colors(null, null, null)

    private val photoDialogBuilder by lazy {
        PhotoDialogBottomSheetFragment.Builder(this)
            .title(R.string.feature_item_creation_add_photo_title)
            .description(R.string.feature_item_creation_add_photo_description)
            .isGuideVisible(false)
            .photoResultListener(viewModel::handlePhoto)
    }

    init {
        navigator.initColorPickerLauncher(this) { viewModel.selectColors(it) }
    }

    override fun onLayoutReady() {
        super.onLayoutReady()
        addStatusBarMargin(clNavHeader)
        addStatusBarMargin(clPhoto)
        clNavHeader.setOnClickListener { }
        val scrollLimit = clPhoto.height - getStatusBarHeight()
        nestedScroll.addScrollLimitListener(scrollLimit) {
            viewHeaderBg.alpha = it
        }
    }

    override fun initView() {
        renderLoader(true)
        btnBack.setOnClickListener { onBackPressed() }
        btnDeletePhoto.setOnClickListener { viewModel.handlePhoto(null) }
        btnAddPhoto.setOnClickListener {
            photoDialogBuilder.build().show(supportFragmentManager)
        }

        sivCategory.setOnClickListener {
            CategoriesBottomSheetFragment.newInstance(categories, selectedCategory)
                .show(supportFragmentManager)
        }
        sivBrand.setOnClickListener {
            BrandBottomSheetFragment.newInstance(brands, selectedBrand)
                .show(supportFragmentManager)
        }
        sivSize.setOnClickListener {
            SizeBottomSheetFragment.newInstance(selectedSize)
                .show(supportFragmentManager)
        }
        sivSeason.setOnClickListener {
            SeasonBottomSheetFragment.newInstance()
                .show(supportFragmentManager)
        }
        sivFit.setOnClickListener {
            FitBottomSheetFragment.newInstance()
                .show(supportFragmentManager)
        }
        sivStyle.setOnClickListener {
            StyleBottomSheetFragment.newInstance(styles, selectedStyle)
                .show(supportFragmentManager)
        }
        sivColor.setOnClickListener {
            navigator.startColorPickerActivity(selectedColors)
        }
        tivModel.getEditText().setOnTextChangedListener { viewModel.updateModelName(it) }
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(updatedAvatarUri, ::handleAvatarUri)
            registerObserver(categories, ::handleCategories)
            registerObserver(brands, ::handleBrands)
            registerObserver(styles, ::handleStyles)
            registerObserver(selectedCategory, ::handleSelectedCategory)
            registerObserver(selectedSubCategory, ::handleSelectedSubCategory)
            registerObserver(selectedBrand, ::handleSelectedBrand)
            registerObserver(selectedSeason, ::handleSelectedSeason)
            registerObserver(selectedFit, ::handleSelectedFit)
            registerObserver(selectedStyle, ::handleSelectedStyle)
            registerObserver(selectedSize, ::handleSelectedSize)
            registerObserver(selectedColors, ::handleSelectedColors)
        }
    }

    private fun handleAvatarUri(uri: Uri?) {
        selectedColors = selectedColors?.copy(imageUri = uri)
        if (uri != null) {
            btnAddPhoto.gone()
            clAddedPhoto.visibleWithAlpha()
            ivPhoto.setImageURI(uri)
        } else {
            clAddedPhoto.gone()
            btnAddPhoto.visibleWithAlpha()
        }
        sivColor.isVisible = uri != null
    }

    private fun handleCategories(categories: List<Categories>) {
        this.categories.replaceAllWith(categories)
    }

    private fun handleBrands(brands: List<Brand>) {
        this.brands.replaceAllWith(brands)
    }

    private fun handleStyles(styles: List<Style>) {
        this.styles.replaceAllWith(styles)
    }

    private fun handleSelectedCategory(category: Category?) {
        if (selectedCategory == category) return
        clearSubCategory()
        selectedCategory = category
        sivCategory.setItem(category?.title)
        if (!category?.childCategories.isNullOrEmpty()) {
            sivSubCategory.visible()
            sivSubCategory.setOnClickListener {
                val list = selectedCategory?.childCategories ?: listOf()
                SubCategoriesBottomSheetFragment.newInstance(ArrayList(list), selectedSubCategory)
                    .show(supportFragmentManager)
            }
        }
    }

    private fun clearSubCategory() {
        sivSubCategory.gone()
        sivSubCategory.setItem(null)
        viewModel.selectSubCategory(null)
        sivSubCategory.setOnClickListener { }
    }

    private fun handleSelectedColors(colors: Colors?) {
        selectedColors = colors
        val list = ArrayList<Int>()
        colors?.mainColor?.apply { list.add(this) }
        colors?.additionalColors?.forEach { list.add(it) }
        sivColor.setItems(list.asReversed())
    }

    private fun handleSelectedSubCategory(subCategory: Category?) {
        selectedSubCategory = subCategory
        sivSubCategory.setItem(subCategory?.title)
    }

    private fun handleSelectedBrand(brand: Brand?) {
        selectedBrand = brand
        sivBrand.setItem(brand?.name)
    }

    private fun handleSelectedStyle(style: Style?) {
        selectedStyle = style
        sivStyle.setItem(style?.name)
    }

    private fun handleSelectedSize(size: Size?) {
        selectedSize = size
        if (size != null) {
            sivSize.setItem(
                size.size.plus(StringConstants.SPACE).plus(size.sizeType.name)
            )
        } else {
            sivSize.setItem(null)
        }
    }

    private fun handleSelectedSeason(season: Season?) {
        val seasonRes = when (season) {
            Season.SUMMER -> R.string.feature_item_creation_season_summer
            Season.WINTER -> R.string.feature_item_creation_season_winter
            Season.DEMISEASON -> R.string.feature_item_creation_season_demiseason
            else -> null
        }
        if (seasonRes != null) {
            sivSeason.setItem(seasonRes)
        } else {
            sivSeason.setItem(null)
        }
    }

    private fun handleSelectedFit(fit: Fit?) {
        val fitRes = when (fit) {
            Fit.SLIM -> R.string.feature_item_creation_fit_slim
            Fit.REGULAR -> R.string.feature_item_creation_fit_regular
            Fit.COMFORT -> R.string.feature_item_creation_fit_comfort
            Fit.OVERSIZE -> R.string.feature_item_creation_fit_oversize
            else -> null
        }
        if (fitRes != null) {
            sivFit.setItem(fitRes)
        } else {
            sivFit.setItem(null)
        }
        sivFit.setItem(fit?.name.orEmpty().lowercase().capitalize())
    }
}