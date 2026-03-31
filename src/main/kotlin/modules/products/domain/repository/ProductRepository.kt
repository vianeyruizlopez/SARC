package modules.products.domain.repository

import modules.products.domain.model.Product
import java.util.UUID

interface ProductRepository {
    suspend fun findByName(name: String): Product?
    suspend fun findById(id: UUID): Product?
    suspend fun findAll(): List<Product>
    suspend fun save(product: Product): Product
    suspend fun update(product: Product): Product?
    suspend fun delete(id: UUID): Boolean
}