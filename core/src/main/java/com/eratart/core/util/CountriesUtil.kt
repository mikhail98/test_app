package com.eratart.core.util

import android.content.Context
import android.content.res.Resources
import com.eratart.core.constants.IntConstants
import java.util.*

object CountriesUtil {

    fun getDefaultLocale(): String =
        Resources.getSystem().configuration.locales.get(IntConstants.ZERO).language

    fun getCountries(): ArrayList<String> {
        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim().isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()
        return countries
    }

    fun Context.getCountryCode(): String =
        resources.configuration.locales.get(IntConstants.ZERO).country.lowercase()
}