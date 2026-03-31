package modules.products.infrastructure.rest.dto

import kotlinx.serialization.Serializable
import modules.products.domain.model.Product

@Serializable
data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
) {
    fun toDomain() = Product(
        name = name,
        description = description,
        price = price,
        stock = stock
    )
}