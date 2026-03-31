package modules.products.application

import modules.products.domain.model.Product
import modules.products.domain.repository.ProductRepository

class CreateProductUseCase(private val repository: ProductRepository) {

    suspend fun execute(product: Product): Product {
        // Regla de negocio: No permitir duplicados por nombre
        val existing = repository.findByName(product.name)
        if (existing != null) {
            throw IllegalStateException("El producto con nombre ${product.name} ya existe")
        }

        return repository.save(product)
    }
}
