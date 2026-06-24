package com.altayyar.app.presentation.ui.feature.orders

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
import com.altayyar.app.databinding.ActivityOrdersBinding
import com.altayyar.app.presentation.ui.activity.BaseActivity
import com.altayyar.app.presentation.ui.feature.orders.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersActivity : BaseActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val buyerId = intent.getStringExtra(EXTRA_BUYER_ID) ?: "buyer-1"

        adapter = OrderAdapter()
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.ordersRecyclerView.adapter = adapter

        viewModel.loadOrders(buyerId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.orders)
                    binding.emptyView.isVisible = state.orders.isEmpty() && !state.isLoading
                    binding.ordersRecyclerView.isVisible = state.orders.isNotEmpty()
                }
            }
        }
    }

    companion object {
        private const val EXTRA_BUYER_ID = "buyer_id"

        fun startIntent(context: Context): Intent {
            return Intent(context, OrdersActivity::class.java).apply {
                putExtra(EXTRA_BUYER_ID, "buyer-1")
            }
        }
    }
}
