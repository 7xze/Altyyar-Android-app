package com.altayyar.app.domain.entity.marketplace

data class MarketplaceService(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val images: List<String> = emptyList(),
    val sellerId: String,
    val sellerName: String,
    val category: ServiceCategory,
    val isActive: Boolean = true
)

enum class ServiceCategory(val displayName: String) {
    DESIGN("تصميم"),
    PHOTOGRAPHY("تصوير"),
    EDITING("تعديل")
}
