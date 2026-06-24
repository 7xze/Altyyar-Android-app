package com.altayyar.app.domain.entity.marketplace

data class Order(
    val id: String,
    val items: List<CartItem>,
    val totalPrice: Double,
    val status: OrderStatus,
    val buyerId: String,
    val createdAt: Long = System.currentTimeMillis()
)

enum class OrderStatus(val displayName: String) {
    PENDING("قيد الانتظار"),
    CONFIRMED("مؤكد"),
    IN_PROGRESS("قيد التنفيذ"),
    COMPLETED("مكتمل"),
    CANCELLED("ملغي")
}
