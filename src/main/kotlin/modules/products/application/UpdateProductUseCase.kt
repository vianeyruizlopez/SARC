package com.alilopez.modules.products.application

import modules.products.domain.model.Product
import modules.products.domain.repository.ProductRepository
import java.util.UUID

class UpdateProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(id: UUID, product: Product): Product {
        // Aseguramos que el ID del dominio coincida con el de la URL
        val productToUpdate = product.copy(id = id)
        return repository.update(productToUpdate)
            ?: throw IllegalArgumentException("Producto con ID $id no encontrado")
    }
}