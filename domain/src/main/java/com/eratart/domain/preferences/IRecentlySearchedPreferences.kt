package com.eratart.domain.preferences

import com.eratart.domain.model.domain.dialogs.SearchedItem

interface IRecentlySearchedPreferences {
    fun get(): List<SearchedItem>
    fun set(items: List<SearchedItem>)
}