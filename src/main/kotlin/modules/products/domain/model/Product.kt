package modules.products.domain.model

import java.util.UUID

data class Product(
    val id: UUID? = null,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
) {
    // Regla de negocio: Un producto no puede tener precio negativo
    init {
        require(price >= 0) { "El precio no puede ser negativo" }
    }
}