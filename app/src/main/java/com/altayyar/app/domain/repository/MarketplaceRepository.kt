package com.altayyar.app.domain.repository

import com.altayyar.app.domain.entity.marketplace.CartItem
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.entity.marketplace.Order
import com.altayyar.app.domain.entity.marketplace.ServiceCategory

interface MarketplaceRepository {
    suspend fun getServices(): List<MarketplaceService>
    suspend fun getServicesByCategory(category: ServiceCategory): List<MarketplaceService>
    suspend fun getServiceById(id: String): MarketplaceService?
    suspend fun searchServices(query: String): List<MarketplaceService>
    suspend fun getCart(): List<CartItem>
    suspend fun addToCart(serviceId: String)
    suspend fun removeFromCart(serviceId: String)
    suspend fun updateCartQuantity(serviceId: String, quantity: Int)
    suspend fun clearCart()
    suspend fun createOrder(buyerId: String): Order
    suspend fun getOrdersByBuyer(buyerId: String): List<Order>
    suspend fun getServicesBySeller(sellerId: String): List<MarketplaceService>
    suspend fun addService(service: MarketplaceService)
    suspend fun updateService(service: MarketplaceService)
    suspend fun deleteService(serviceId: String)
}
