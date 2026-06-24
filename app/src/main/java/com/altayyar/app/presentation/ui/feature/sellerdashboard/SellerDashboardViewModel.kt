package com.altayyar.app.presentation.ui.feature.sellerdashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerDashboardUiState(
    val services: List<MarketplaceService> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerDashboardUiState())
    val uiState: StateFlow<SellerDashboardUiState> = _uiState

    fun loadServices(sellerId: String) {
        viewModelScope.launch {
            _uiState.value = SellerDashboardUiState(isLoading = true)
            val services = repository.getServicesBySeller(sellerId)
            _uiState.value = SellerDashboardUiState(services = services, isLoading = false)
        }
    }

    fun deleteService(serviceId: String, sellerId: String) {
        viewModelScope.launch {
            repository.deleteService(serviceId)
            loadServices(sellerId)
        }
    }
}
