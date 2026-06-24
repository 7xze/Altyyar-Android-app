package com.altayyar.app.presentation.ui.feature.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ServiceDetailUiState(
    val service: MarketplaceService? = null,
    val isLoading: Boolean = true,
    val addedToCart: Boolean = false
)

@HiltViewModel
class ServiceDetailViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceDetailUiState())
    val uiState: StateFlow<ServiceDetailUiState> = _uiState

    fun loadService(serviceId: String) {
        viewModelScope.launch {
            _uiState.value = ServiceDetailUiState(isLoading = true)
            val service = repository.getServiceById(serviceId)
            _uiState.value = ServiceDetailUiState(service = service, isLoading = false)
        }
    }

    fun addToCart(serviceId: String) {
        viewModelScope.launch {
            repository.addToCart(serviceId)
            _uiState.value = _uiState.value.copy(addedToCart = true)
        }
    }
}
