package com.eratart.domain.interactor.other

import com.eratart.domain.model.domain.Style

object MockUtil {

    fun getStyles(): List<Style> {
        val stylesList = ArrayList<Style>()

        stylesList.add(Style(0, "Military"))
        stylesList.add(Style(1, "Casual"))
        stylesList.add(Style(2, "Business"))
        stylesList.add(Style(3, "Sport"))
        stylesList.add(Style(4, "Adidas"))
        stylesList.add(Style(5, "Classic"))
        stylesList.add(Style(6, "Exotic"))
        stylesList.add(Style(7, "Vintage"))
        stylesList.add(Style(8, "Elegance"))

        return stylesList
    }
}