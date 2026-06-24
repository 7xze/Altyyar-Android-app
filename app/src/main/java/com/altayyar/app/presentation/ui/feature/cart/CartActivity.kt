package com.altayyar.app.presentation.ui.feature.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityCartBinding
import com.altayyar.app.presentation.ui.activity.BaseActivity
import com.altayyar.app.presentation.ui.feature.cart.CartViewModel
import com.altayyar.app.presentation.ui.feature.cart.adapter.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        binding.checkoutButton.setOnClickListener {
            val accountId = accountManager.activeAccount?.accountId?.toString() ?: "buyer-1"
            viewModel.checkout(accountId)
        }

        observeState()
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            onQuantityChanged = { serviceId, quantity ->
                viewModel.updateQuantity(serviceId, quantity)
            },
            onRemove = { serviceId ->
                viewModel.removeFromCart(serviceId)
            }
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.items)
                    binding.totalPrice.text = getString(R.string.marketplace_total, getString(R.string.marketplace_price, state.totalPrice.toString()))
                    binding.emptyView.isVisible = state.items.isEmpty()
                    binding.cartRecyclerView.isVisible = state.items.isNotEmpty()
                    binding.cartSummaryCard.isVisible = state.items.isNotEmpty()
                    if (state.orderCreated) {
                        Toast.makeText(this@CartActivity, R.string.marketplace_order_created, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        fun startIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java)
        }
    }
}
