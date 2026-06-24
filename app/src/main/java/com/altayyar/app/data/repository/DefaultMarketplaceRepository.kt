package com.altayyar.app.data.repository

import com.altayyar.app.domain.entity.marketplace.CartItem
import com.altayyar.app.domain.entity.marketplace.MarketplaceService
import com.altayyar.app.domain.entity.marketplace.Order
import com.altayyar.app.domain.entity.marketplace.OrderStatus
import com.altayyar.app.domain.entity.marketplace.ServiceCategory
import com.altayyar.app.domain.repository.MarketplaceRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultMarketplaceRepository @Inject constructor() : MarketplaceRepository {

    private val services = mutableListOf<MarketplaceService>()
    private val cart = mutableListOf<CartItem>()
    private val orders = mutableListOf<Order>()

    init {
        seedServices()
    }

    override suspend fun getServices(): List<MarketplaceService> = services.filter { it.isActive }

    override suspend fun getServicesByCategory(category: ServiceCategory): List<MarketplaceService> =
        services.filter { it.category == category && it.isActive }

    override suspend fun getServiceById(id: String): MarketplaceService? =
        services.find { it.id == id && it.isActive }

    override suspend fun searchServices(query: String): List<MarketplaceService> {
        val q = query.lowercase()
        return services.filter { it.isActive && (it.title.lowercase().contains(q) || it.description.lowercase().contains(q)) }
    }

    override suspend fun getCart(): List<CartItem> = cart.toList()

    override suspend fun addToCart(serviceId: String) {
        val existing = cart.find { it.serviceId == serviceId }
        if (existing != null) {
            cart.remove(existing)
            cart.add(existing.copy(quantity = existing.quantity + 1))
        } else {
            cart.add(CartItem(serviceId = serviceId, quantity = 1))
        }
    }

    override suspend fun removeFromCart(serviceId: String) {
        cart.removeAll { it.serviceId == serviceId }
    }

    override suspend fun updateCartQuantity(serviceId: String, quantity: Int) {
        val index = cart.indexOfFirst { it.serviceId == serviceId }
        if (index >= 0) {
            if (quantity <= 0) cart.removeAt(index)
            else cart[index] = cart[index].copy(quantity = quantity)
        }
    }

    override suspend fun clearCart() = cart.clear()

    override suspend fun createOrder(buyerId: String): Order {
        val items = cart.toList()
        val total = items.sumOf { item ->
            val service = services.find { it.id == item.serviceId }
            (service?.price ?: 0.0) * item.quantity
        }
        val order = Order(
            id = UUID.randomUUID().toString(),
            items = items,
            totalPrice = total,
            status = OrderStatus.PENDING,
            buyerId = buyerId
        )
        orders.add(order)
        cart.clear()
        return order
    }

    override suspend fun getOrdersByBuyer(buyerId: String): List<Order> =
        orders.filter { it.buyerId == buyerId }.sortedByDescending { it.createdAt }

    override suspend fun getServicesBySeller(sellerId: String): List<MarketplaceService> =
        services.filter { it.sellerId == sellerId }

    override suspend fun addService(service: MarketplaceService) {
        services.add(service)
    }

    override suspend fun updateService(service: MarketplaceService) {
        val index = services.indexOfFirst { it.id == service.id }
        if (index >= 0) services[index] = service
    }

    override suspend fun deleteService(serviceId: String) {
        val index = services.indexOfFirst { it.id == serviceId }
        if (index >= 0) services[index] = services[index].copy(isActive = false)
    }

    private fun seedServices() {
        services.addAll(
            listOf(
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تصميم شعار احترافي",
                    description = "أصمم لك شعاراً احترافياً يعبر عن هوية علامتك التجارية بأحدث الأساليب التصميمية",
                    price = 150.0,
                    sellerId = "seller-1",
                    sellerName = "أحمد المصمم",
                    category = ServiceCategory.DESIGN,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تصميم بوسترات للسوشيال ميديا",
                    description = "تصميم بوسترات إعلانية متكاملة لمنصات التواصل الاجتماعي بجودة عالية",
                    price = 80.0,
                    sellerId = "seller-1",
                    sellerName = "أحمد المصمم",
                    category = ServiceCategory.DESIGN,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تصوير منتجات تجارية",
                    description = "جلسة تصوير احترافية للمنتجات مع إضاءة مثالية وخلفيات متنوعة",
                    price = 200.0,
                    sellerId = "seller-2",
                    sellerName = "سارة المصورة",
                    category = ServiceCategory.PHOTOGRAPHY,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تعديل صور احترافي",
                    description = "معالجة وتحرير الصور بإحترافية باستخدام أدوات التعديل المتقدمة",
                    price = 60.0,
                    sellerId = "seller-2",
                    sellerName = "سارة المصورة",
                    category = ServiceCategory.EDITING,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "هوية بصرية متكاملة",
                    description = "تصميم هوية بصرية كاملة تشمل الشعار، بطاقات العمل، القرطاسية، ودليل الهوية",
                    price = 500.0,
                    sellerId = "seller-1",
                    sellerName = "أحمد المصمم",
                    category = ServiceCategory.DESIGN,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تصوير فعاليات ومناسبات",
                    description = "تغطية تصويرية متكاملة للفعاليات والمناسبات مع أفضل زوايا التصوير",
                    price = 350.0,
                    sellerId = "seller-2",
                    sellerName = "سارة المصورة",
                    category = ServiceCategory.PHOTOGRAPHY,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "فيديو مونتاج دعائي",
                    description = "مونتاج فيديوهات دعائية مع مؤثرات بصرية وصوتية احترافية",
                    price = 250.0,
                    sellerId = "seller-3",
                    sellerName = "محمد المونتير",
                    category = ServiceCategory.EDITING,
                    images = emptyList()
                ),
                MarketplaceService(
                    id = UUID.randomUUID().toString(),
                    title = "تصميم واجهات تطبيقات",
                    description = "تصميم واجهات مستخدم حديثة للتطبيقات مع تجربة مستخدم سلسة",
                    price = 400.0,
                    sellerId = "seller-3",
                    sellerName = "محمد المونتير",
                    category = ServiceCategory.DESIGN,
                    images = emptyList()
                )
            )
        )
    }
}
