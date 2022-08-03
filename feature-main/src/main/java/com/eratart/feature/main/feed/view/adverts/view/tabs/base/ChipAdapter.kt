package com.eratart.feature.main.feed.view.adverts.view.tabs.base

import com.eratart.subfeature.chips.api.BaseChipAdapter
import com.eratart.subfeature.chips.api.ChipItem

class ChipAdapter(viewModels: MutableList<ChipItem<String>>) :
    BaseChipAdapter<String, ChipItem<String>>(viewModels)