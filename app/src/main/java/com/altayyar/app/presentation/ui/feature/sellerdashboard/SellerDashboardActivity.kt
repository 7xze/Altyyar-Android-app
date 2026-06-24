package com.altayyar.app.presentation.ui.feature.sellerdashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivitySellerDashboardBinding
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.presentation.ui.activity.BaseActivity
import com.altayyar.app.presentation.ui.feature.marketplace.adapter.ServiceAdapter
import com.altayyar.app.util.startActivityWithSlideInAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SellerDashboardActivity : BaseActivity() {

    private lateinit var binding: ActivitySellerDashboardBinding
    private val viewModel: SellerDashboardViewModel by viewModels()
    private lateinit var adapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sellerId = intent.getStringExtra(EXTRA_SELLER_ID) ?: "seller-1"

        setupRecyclerView()

        binding.addServiceFab.setOnClickListener {
            startActivityWithSlideInAnimation(AddServiceActivity.newIntent(this, sellerId))
        }

        viewModel.loadServices(sellerId)
        observeState()
    }

    override fun onResume() {
        super.onResume()
        val sellerId = intent.getStringExtra(EXTRA_SELLER_ID) ?: "seller-1"
        viewModel.loadServices(sellerId)
    }

    private fun setupRecyclerView() {
        adapter = ServiceAdapter { service ->
            startActivityWithSlideInAnimation(AddServiceActivity.newEditIntent(this, service.id))
        }
        binding.servicesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.servicesRecyclerView.adapter = adapter
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
        private const val EXTRA_SELLER_ID = "seller_id"

        fun startIntent(context: Context): Intent {
            return Intent(context, SellerDashboardActivity::class.java).apply {
                putExtra(EXTRA_SELLER_ID, "seller-1")
            }
        }
    }
}
