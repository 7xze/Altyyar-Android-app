package com.altayyar.app.presentation.ui.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.Order
import com.altayyar.app.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState

    fun loadOrders(buyerId: String) {
        viewModelScope.launch {
            _uiState.value = OrdersUiState(isLoading = true)
            val orders = repository.getOrdersByBuyer(buyerId)
            _uiState.value = OrdersUiState(orders = orders, isLoading = false)
        }
    }
}
