package com.altayyar.app.presentation.ui.feature.marketplace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityServiceDetailBinding
import com.altayyar.app.presentation.ui.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServiceDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityServiceDetailBinding
    private val viewModel: ServiceDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val serviceId = intent.getStringExtra(EXTRA_SERVICE_ID) ?: return finish()
        viewModel.loadService(serviceId)

        binding.addToCartButton.setOnClickListener {
            viewModel.addToCart(serviceId)
        }

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.service?.let { service ->
                        binding.serviceTitle.text = service.title
                        binding.serviceDescription.text = service.description
                        binding.servicePrice.text = getString(R.string.marketplace_price, service.price.toString())
                        binding.serviceCategory.text = service.category.displayName
                        binding.sellerName.text = service.sellerName
                    }
                    if (state.addedToCart) {
                        Toast.makeText(this@ServiceDetailActivity, R.string.marketplace_added_to_cart, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_SERVICE_ID = "service_id"

        fun startIntent(context: Context, serviceId: String): Intent {
            return Intent(context, ServiceDetailActivity::class.java).apply {
                putExtra(EXTRA_SERVICE_ID, serviceId)
            }
        }
    }
}
