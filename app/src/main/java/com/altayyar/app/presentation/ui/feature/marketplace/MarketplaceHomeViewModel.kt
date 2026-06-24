package com.altayyar.app.presentation.ui.feature.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.entity.marketplace.ServiceCategory
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarketplaceUiState(
    val services: List<MarketplaceService> = emptyList(),
    val selectedCategory: ServiceCategory? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class MarketplaceHomeViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState

    init {
        loadServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val allServices = repository.getServices()
            _uiState.value = _uiState.value.copy(
                services = allServices,
                isLoading = false
            )
            applyFilters()
        }
    }

    fun filterByCategory(category: ServiceCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val state = _uiState.value
            var result = repository.getServices()

            if (state.selectedCategory != null) {
                result = result.filter { it.category == state.selectedCategory }
            }
            if (state.searchQuery.isNotBlank()) {
                result = repository.searchServices(state.searchQuery)
                if (state.selectedCategory != null) {
                    result = result.filter { it.category == state.selectedCategory }
                }
            }

            _uiState.value = _uiState.value.copy(services = result)
        }
    }
}
