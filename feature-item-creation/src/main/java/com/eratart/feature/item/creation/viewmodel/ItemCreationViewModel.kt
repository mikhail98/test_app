package com.eratart.feature.item.creation.viewmodel

import android.net.Uri
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.delayedLaunch
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.other.IOtherInteractor
import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Category
import com.eratart.domain.model.domain.Colors
import com.eratart.domain.model.domain.Size
import com.eratart.domain.model.domain.Style
import com.eratart.domain.model.enums.Fit
import com.eratart.domain.model.enums.Season
import com.eratart.feature.item.creation.entity.ParamsEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ItemCreationViewModel(
    private val otherInteractor: IOtherInteractor,
) : BaseViewModel() {

    private val _updatedAvatarUri = MutableSharedFlow<Uri?>()
    val updatedAvatarUri = _updatedAvatarUri.asSharedFlow()

    private val _styles = MutableSharedFlow<List<Style>>()
    val styles = _styles.asSharedFlow()

    private val _brands = MutableSharedFlow<List<Brand>>()
    val brands = _brands.asSharedFlow()

    private val _categories = MutableSharedFlow<List<Categories>>()
    val categories = _categories.asSharedFlow()

    private val _selectedCategory = MutableSharedFlow<Category?>()
    val selectedCategory = _selectedCategory.asSharedFlow()

    private val _selectedSubCategory = MutableSharedFlow<Category?>()
    val selectedSubCategory = _selectedSubCategory.asSharedFlow()

    private val _selectedBrand = MutableSharedFlow<Brand?>()
    val selectedBrand = _selectedBrand.asSharedFlow()

    private val _selectedSeason = MutableSharedFlow<Season?>()
    val selectedSeason = _selectedSeason.asSharedFlow()

    private val _selectedFit = MutableSharedFlow<Fit?>()
    val selectedFit = _selectedFit.asSharedFlow()

    private val _selectedStyle = MutableSharedFlow<Style?>()
    val selectedStyle = _selectedStyle.asSharedFlow()

    private val _selectedSize = MutableSharedFlow<Size?>()
    val selectedSize = _selectedSize.asSharedFlow()

    private val _selectedColors = MutableSharedFlow<Colors?>()
    val selectedColors = _selectedColors.asSharedFlow()

    private var newAvatarUri: Uri? = null
    private var newSelectedCategory: Category? = null
    private var newSelectedSubCategory: Category? = null
    private var newModelName: String? = null
    private var newBrand: Brand? = null
    private var newSeason: Season? = null
    private var newFit: Fit? = null
    private var newStyle: Style? = null
    private var newSize: Size? = null
    private var newColors: Colors? = null

    fun handlePhoto(uri: Uri?) {
        newAvatarUri = uri
        delayedLaunch { _updatedAvatarUri.emit(uri) }
    }

    override fun onCreate() {
        super.onCreate()
        loadCategories()
    }

    private fun loadCategories() {
        launchFlow {
            otherInteractor.getCategories().zip(otherInteractor.getBrands()) { data1, data2 ->
                ParamsEntity().copy(categories = data1, brands = data2)
            }.zip(otherInteractor.getStyles()) { data1, data2 ->
                data1.copy(styles = data2)
            }
                .applyLoader()
                .subscribeWithError {
                    _categories.emit(it.categories)
                    _brands.emit(it.brands)
                    _styles.emit(it.styles)
                }
        }
    }

    fun selectCategory(category: Category?) {
        newSelectedCategory = category
        launch { _selectedCategory.emit(category) }
    }

    fun selectSubCategory(category: Category?) {
        newSelectedSubCategory = category
        launch { _selectedSubCategory.emit(category) }
    }

    fun selectBrand(brand: Brand?) {
        newBrand = brand
        launch { _selectedBrand.emit(brand) }
    }

    fun selectSeason(season: Season?) {
        newSeason = season
        launch { _selectedSeason.emit(season) }
    }

    fun selectFit(fit: Fit?) {
        newFit = fit
        launch { _selectedFit.emit(fit) }
    }

    fun selectStyle(style: Style?) {
        newStyle = style
        launch { _selectedStyle.emit(style) }
    }

    fun selectSize(size: Size?) {
        newSize = size
        launch { _selectedSize.emit(size) }
    }

    fun selectColors(colors: Colors?) {
        newColors = colors
        delayedLaunch { _selectedColors.emit(colors) }
    }

    fun updateModelName(modelName: String) {
        this.newModelName = modelName.ifEmpty { null }
    }
}