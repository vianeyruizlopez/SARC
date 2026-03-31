package modules.products.infrastructure.persistence

import modules.products.domain.model.Product
import modules.products.domain.repository.ProductRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryProductRepository : ProductRepository {
    private val storage = ConcurrentHashMap<UUID, Product>()

    override suspend fun save(product: Product): Product {
        val id = product.id ?: UUID.randomUUID()
        val toSave = product.copy(id = id)
        storage[id] = toSave
        return toSave
    }

    override suspend fun findByName(name: String): Product? {
        // Buscamos en los valores del mapa el primer producto que coincida
        // .find devuelve null si no encuentra nada, lo cual cumple con el contrato
        return storage.values.find { it.name.equals(name, ignoreCase = true) }
    }

    override suspend fun findById(id: UUID): Product? = storage[id]
    override suspend fun findAll(): List<Product> = storage.values.toList()
    override suspend fun update(product: Product): Product? {
        val id = product.id ?: return null
        if (!storage.containsKey(id)) return null
        storage[id] = product
        return product
    }

    override suspend fun delete(id: UUID): Boolean {
        return storage.remove(id) != null
    }
}