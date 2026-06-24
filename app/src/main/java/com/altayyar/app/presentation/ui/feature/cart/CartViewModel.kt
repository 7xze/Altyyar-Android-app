package com.altayyar.app.presentation.ui.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altayyar.app.domain.entity.marketplace.CartItem
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.repository.MarketplaceRepository
import com.altayyar.app.presentation.ui.feature.cart.adapter.CartDisplayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val items: List<CartDisplayItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val orderCreated: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            val cartItems = repository.getCart()
            val displayItems = cartItems.mapNotNull { item ->
                val service = repository.getServiceById(item.serviceId)
                service?.let { CartDisplayItem(item, it) }
            }
            val total = displayItems.sumOf { it.service.price * it.cartItem.quantity }
            _uiState.value = CartUiState(items = displayItems, totalPrice = total)
        }
    }

    fun updateQuantity(serviceId: String, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                repository.removeFromCart(serviceId)
            } else {
                repository.updateCartQuantity(serviceId, quantity)
            }
            loadCart()
        }
    }

    fun removeFromCart(serviceId: String) {
        viewModelScope.launch {
            repository.removeFromCart(serviceId)
            loadCart()
        }
    }

    fun checkout(buyerId: String) {
        viewModelScope.launch {
            repository.createOrder(buyerId)
            _uiState.value = _uiState.value.copy(orderCreated = true)
        }
    }
}
