package com.altayyar.app.presentation.ui.feature.sellerdashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.entity.marketplace.ServiceCategory
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class AddServiceEvent {
    data object ServiceSaved : AddServiceEvent()
}

@HiltViewModel
class AddServiceViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<AddServiceEvent>()
    val events: SharedFlow<AddServiceEvent> = _events

    private var editingServiceId: String? = null
    var isEditMode: Boolean = false

    fun loadForEdit(serviceId: String) {
        viewModelScope.launch {
            val service = repository.getServiceById(serviceId)
            service?.let {
                editingServiceId = it.id
                isEditMode = true
            }
        }
    }

    fun getEditableService(serviceId: String, callback: (MarketplaceService?) -> Unit) {
        viewModelScope.launch {
            callback(repository.getServiceById(serviceId))
        }
    }

    fun saveService(
        sellerId: String,
        title: String,
        description: String,
        price: Double,
        category: ServiceCategory
    ) {
        viewModelScope.launch {
            val existing = editingServiceId?.let { repository.getServiceById(it) }
            if (existing != null) {
                repository.updateService(
                    existing.copy(
                        title = title,
                        description = description,
                        price = price,
                        category = category
                    )
                )
            } else {
                val service = MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    price = price,
                    sellerId = sellerId,
                    sellerName = sellerId,
                    category = category,
                    images = emptyList()
                )
                repository.addService(service)
            }
            _events.emit(AddServiceEvent.ServiceSaved)
        }
    }
}
