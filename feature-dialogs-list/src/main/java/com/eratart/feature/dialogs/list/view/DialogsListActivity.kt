package com.eratart.feature.dialogs.list.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.eratart.baseui.base.activity.BaseActivity
import com.eratart.baseui.extensions.fadeInAnimation
import com.eratart.baseui.extensions.fadeOutAnimation
import com.eratart.baseui.extensions.registerObserver
import com.eratart.feature.dialogs.list.R
import com.eratart.feature.dialogs.list.databinding.ActivityDialogsListBinding
import com.eratart.feature.dialogs.list.di.DialogsListModule
import com.eratart.feature.dialogs.list.view.model.DialogsListEntity
import com.eratart.feature.dialogs.list.view.recycler.DialogsListAdapter
import com.eratart.feature.dialogs.list.view.recycler.OnDialogClickListener
import com.eratart.feature.dialogs.list.view.recycler.SearchItemActionClickListener
import com.eratart.feature.dialogs.list.viewmodel.DialogsListViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogsListActivity :
    BaseActivity<DialogsListViewModel, ActivityDialogsListBinding>(R.layout.activity_dialogs_list) {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, DialogsListActivity::class.java)
        }

        private const val TEXT_UPDATES_DELAY = 450L
    }

    override val koinModules = listOf(DialogsListModule)
    override val viewModel: DialogsListViewModel by viewModel()
    override val binding: ActivityDialogsListBinding by viewBinding()

    private val searchBar by lazy { binding.searchBar }
    private val tvTitle by lazy { binding.tvTitle }
    private val btnSearch by lazy { binding.btnSearch }
    private val btnBack by lazy { binding.btnBack }
    private val rvDialogs by lazy { binding.rvDialogs }

    private val searchItemActionClickListener = SearchItemActionClickListener { searchItem ->
        viewModel.handleOnSearchItemActionClick(searchItem)
    }

    private val onDialogClickListener = OnDialogClickListener {
        navigator.startDialogActivity(this)
    }

    private val adapter by lazy {
        DialogsListAdapter(
            searchItemActionClickListener,
            onDialogClickListener
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData(false)
    }

    override fun initView() {
        rvDialogs.adapter = adapter
        btnBack.setOnClickListener {
            viewModel.onBackButtonClicked(searchBar.isActive)
        }
        btnSearch.setOnClickListener {
            viewModel.onSearchButtonClicked()
        }
        searchBar.setOnSearchClickListener(viewModel::searchTextUpdated)
    }

    override fun initViewModel() {
        viewModel.apply {
            registerObserver(screenInfo, ::handleScreenInfo)
            registerObserver(isSearchActive, ::handleSearchState)
            registerObserver(onNextInSearchItemClick, ::handleOnNextInSearchItem)
        }
    }

    private fun handleOnNextInSearchItem(click: Boolean) {
        navigator.startDialogActivity(this)
    }

    private fun handleSearchState(isActive: Boolean) {
        tvTitle.isVisible = !isActive
        if (isActive) {
            handleSearchActiveState()
        } else {
            handleSearchUnactiveState()
        }
    }

    private fun handleSearchActiveState() {
        val searchFlow = searchBar
            .subscribeTextUpdated(TEXT_UPDATES_DELAY)
            .mapLatest(viewModel::searchTextUpdated)

        btnSearch.fadeOutAnimation {
            searchBar.fadeInAnimation {
                viewModel.loadData(isSearch = true)
                searchBar.isActive = true
                searchFlow.launchIn(lifecycleScope)
            }
        }
    }

    private fun handleSearchUnactiveState() {
        viewModel.loadData(isSearch = false)
        searchBar.isActive = false
        searchBar.fadeOutAnimation {
            btnSearch.fadeInAnimation()
        }
    }

    private fun handleScreenInfo(dialogsListEntity: DialogsListEntity) {
        binding.tvRecentlySearch.isVisible = dialogsListEntity.isRecentlySearchVisible
        adapter.replaceAll(dialogsListEntity.items)
    }

    override fun onBackPressed() {
        viewModel.onBackButtonClicked(searchBar.isActive)
    }
}