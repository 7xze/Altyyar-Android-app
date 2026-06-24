package com.altayyar.app.presentation.ui.feature.marketplace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.core.view.isVisible
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityMarketplaceHomeBinding
import com.altayyar.app.domain.entity.marketplace.ServiceCategory
import com.altayyar.app.presentation.ui.activity.BaseActivity
import com.altayyar.app.presentation.ui.feature.marketplace.adapter.ServiceAdapter
import com.altayyar.app.presentation.ui.feature.cart.CartActivity
import com.altayyar.app.util.startActivityWithSlideInAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketplaceHomeActivity : BaseActivity() {

    private lateinit var binding: ActivityMarketplaceHomeBinding
    private val viewModel: MarketplaceHomeViewModel by viewModels()
    private lateinit var adapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketplaceHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        setupCategoryChips()
        setupSearch()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = ServiceAdapter { service ->
            startActivityWithSlideInAnimation(ServiceDetailActivity.startIntent(this, service.id))
        }
        binding.servicesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.servicesRecyclerView.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.chipAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.filterByCategory(null)
        }
        binding.chipDesign.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.filterByCategory(ServiceCategory.DESIGN)
        }
        binding.chipPhotography.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.filterByCategory(ServiceCategory.PHOTOGRAPHY)
        }
        binding.chipEditing.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.filterByCategory(ServiceCategory.EDITING)
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.search(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.services)
                    binding.emptyView.isVisible = state.services.isEmpty() && !state.isLoading
                    binding.servicesRecyclerView.isVisible = state.services.isNotEmpty()
                }
            }
        }
    }

    companion object {
        fun startIntent(context: android.content.Context): Intent {
            return Intent(context, MarketplaceHomeActivity::class.java)
        }
    }
}
