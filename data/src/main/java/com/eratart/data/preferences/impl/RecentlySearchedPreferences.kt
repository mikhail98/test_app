package com.eratart.data.preferences.impl

import com.eratart.data.mock.DialogsMock
import com.eratart.domain.model.domain.dialogs.SearchedItem
import com.eratart.domain.preferences.IRecentlySearchedPreferences

class RecentlySearchedPreferences : IRecentlySearchedPreferences {
    private val mock = DialogsMock()

    override fun get(): List<SearchedItem> {
        return mock.getRecentlySearched()
    }

    override fun set(items: List<SearchedItem>) {
        TODO("Not yet implemented")
    }
}