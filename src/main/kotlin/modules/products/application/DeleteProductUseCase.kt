package com.alilopez.modules.products.application

import modules.products.domain.repository.ProductRepository
import java.util.UUID

class DeleteProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(id: UUID) {
        val deleted = repository.delete(id)
        if (!deleted) throw IllegalArgumentException("No se pudo eliminar: Producto $id no existe")
    }
}