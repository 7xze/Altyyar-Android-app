package com.altayyar.app.components.filters

import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.altayyar.app.BaseActivity
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityFiltersBinding
import com.altayyar.app.entity.Filter
import com.altayyar.app.util.ensureBottomMargin
import com.altayyar.app.util.ensureBottomPadding
import com.altayyar.app.util.hide
import com.altayyar.app.util.launchAndRepeatOnLifecycle
import com.altayyar.app.util.show
import com.altayyar.app.util.viewBinding
import com.altayyar.app.util.visible
import com.altayyar.app.util.withSlideInAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FiltersActivity : BaseActivity(), FiltersListener {

    private val binding by viewBinding(ActivityFiltersBinding::inflate)
    private val viewModel: FiltersViewModel by viewModels()

    private val editFilterLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // refresh the filters upon returning from EditFilterActivity
            reloadFilters()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            // Back button
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.filtersList.ensureBottomPadding(fab = true)
        binding.addFilterButton.ensureBottomMargin()

        binding.addFilterButton.setOnClickListener {
            launchEditFilterActivity()
        }

        binding.swipeRefreshLayout.setOnRefreshListener { reloadFilters() }

        setTitle(R.string.pref_title_timeline_filters)

        binding.filtersList.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        observeViewModel()
    }

    private fun observeViewModel() {
        launchAndRepeatOnLifecycle {
            viewModel.state.collect { state ->
                binding.progressBar.visible(
                    state.loadingState == FiltersViewModel.LoadingState.LOADING
                )
                binding.swipeRefreshLayout.isRefreshing = state.loadingState == FiltersViewModel.LoadingState.LOADING
                binding.addFilterButton.visible(
                    state.loadingState == FiltersViewModel.LoadingState.LOADED
                )

                when (state.loadingState) {
                    FiltersViewModel.LoadingState.INITIAL, FiltersViewModel.LoadingState.LOADING -> binding.messageView.hide()
                    FiltersViewModel.LoadingState.ERROR_NETWORK -> {
                        binding.messageView.setup(
                            R.drawable.errorphant_offline,
                            R.string.error_network
                        ) {
                            reloadFilters()
                        }
                        binding.messageView.show()
                    }
                    FiltersViewModel.LoadingState.ERROR_OTHER -> {
                        binding.messageView.setup(
                            R.drawable.errorphant_error,
                            R.string.error_generic
                        ) {
                            reloadFilters()
                        }
                        binding.messageView.show()
                    }
                    FiltersViewModel.LoadingState.LOADED -> {
                        binding.filtersList.adapter = FiltersAdapter(this@FiltersActivity, state.filters)
                        if (state.filters.isEmpty()) {
                            binding.messageView.setup(
                                R.drawable.elephant_friend_empty,
                                R.string.message_empty,
                                null
                            )
                            binding.messageView.show()
                        } else {
                            binding.messageView.hide()
                        }
                    }
                }
            }
        }
    }

    private fun reloadFilters() {
        viewModel.reload()
    }

    private fun launchEditFilterActivity(filter: Filter? = null) {
        val intent = Intent(this, EditFilterActivity::class.java).apply {
            if (filter != null) {
                putExtra(EditFilterActivity.FILTER_TO_EDIT, filter)
            }
        }.withSlideInAnimation()
        editFilterLauncher.launch(intent)
    }

    override fun deleteFilter(filter: Filter) {
        lifecycleScope.launch {
            if (showDeleteFilterDialog(filter.title) == BUTTON_POSITIVE) {
                viewModel.deleteFilter(filter, binding.root)
            }
        }
    }

    override fun updateFilter(updatedFilter: Filter) {
        launchEditFilterActivity(updatedFilter)
    }
}
