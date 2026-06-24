package com.altayyar.app.presentation.ui.feature.sellerdashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityAddServiceBinding
import com.altayyar.app.domain.entity.marketplace.ServiceCategory
import com.altayyar.app.presentation.ui.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddServiceActivity : BaseActivity() {

    private lateinit var binding: ActivityAddServiceBinding
    private val viewModel: AddServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sellerId = intent.getStringExtra(EXTRA_SELLER_ID) ?: "seller-1"
        val editServiceId = intent.getStringExtra(EXTRA_EDIT_SERVICE_ID)

        if (editServiceId != null) {
            binding.toolbar.setTitle(R.string.marketplace_edit_service)
            viewModel.loadForEdit(editServiceId)
            viewModel.getEditableService(editServiceId) { service ->
                service?.let {
                    binding.titleInput.setText(it.title)
                    binding.descriptionInput.setText(it.description)
                    binding.priceInput.setText(it.price.toBigDecimal().stripTrailingZeros().toPlainString())
                    when (it.category) {
                        ServiceCategory.DESIGN -> binding.chipDesign.isChecked = true
                        ServiceCategory.PHOTOGRAPHY -> binding.chipPhotography.isChecked = true
                        ServiceCategory.EDITING -> binding.chipEditing.isChecked = true
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleInput.text?.toString()?.trim() ?: ""
            val description = binding.descriptionInput.text?.toString()?.trim() ?: ""
            val priceText = binding.priceInput.text?.toString()?.trim() ?: ""
            val category = when (binding.categoryChipGroup.checkedChipId) {
                binding.chipDesign.id -> ServiceCategory.DESIGN
                binding.chipPhotography.id -> ServiceCategory.PHOTOGRAPHY
                binding.chipEditing.id -> ServiceCategory.EDITING
                else -> null
            }

            if (title.isEmpty() || description.isEmpty() || priceText.isEmpty() || category == null) {
                Toast.makeText(this, R.string.error_generic, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveService(sellerId, title, description, priceText.toDoubleOrNull() ?: 0.0, category)
        }

        observeEvents()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        AddServiceEvent.ServiceSaved -> {
                            Toast.makeText(
                                this@AddServiceActivity,
                                if (viewModel.isEditMode) R.string.marketplace_service_updated else R.string.marketplace_service_added,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_SELLER_ID = "seller_id"
        private const val EXTRA_EDIT_SERVICE_ID = "edit_service_id"

        fun newIntent(context: Context, sellerId: String): Intent {
            return Intent(context, AddServiceActivity::class.java).apply {
                putExtra(EXTRA_SELLER_ID, sellerId)
            }
        }

        fun newEditIntent(context: Context, serviceId: String): Intent {
            return Intent(context, AddServiceActivity::class.java).apply {
                putExtra(EXTRA_EDIT_SERVICE_ID, serviceId)
            }
        }
    }
}
