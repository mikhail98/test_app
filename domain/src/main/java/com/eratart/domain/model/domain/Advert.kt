package com.eratart.domain.model.domain

import android.os.Parcelable
import com.eratart.domain.model.enums.Currency
import com.eratart.domain.model.enums.MyAdvertState
import kotlinx.parcelize.Parcelize

@Parcelize
data class Advert(
    val id: Long,
    val price: Price,
    val item: Item,
    val myAdvertState: MyAdvertState?,
    var isFavorite: Boolean,
) : Parcelable

@Parcelize
data class Price(
    val amount: Float,
    val currency: Currency,
) : Parcelable

