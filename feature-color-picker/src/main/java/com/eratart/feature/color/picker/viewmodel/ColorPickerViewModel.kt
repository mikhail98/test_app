package com.eratart.feature.color.picker.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ColorPickerViewModel : BaseViewModel() {

    private val _mainColor = MutableSharedFlow<Int?>()
    val mainColor = _mainColor.asSharedFlow()

    private val _additionalColor1 = MutableSharedFlow<Int?>()
    val additionalColor1 = _additionalColor1.asSharedFlow()

    private val _additionalColor2 = MutableSharedFlow<Int?>()
    val additionalColor2 = _additionalColor2.asSharedFlow()

    private var newMainColor: Int? = null
    private var newAdditionalColor1: Int? = null
    private var newAdditionalColor2: Int? = null

    fun selectMainColor(color: Int?) {
        newMainColor = color
        launch { _mainColor.emit(color) }
    }

    fun selectAdditionalColor1(color: Int?) {
        newAdditionalColor1 = color
        launch { _additionalColor1.emit(color) }
    }

    fun selectAdditionalColor2(color: Int?) {
        newAdditionalColor2 = color
        launch { _additionalColor2.emit(color) }
    }
}