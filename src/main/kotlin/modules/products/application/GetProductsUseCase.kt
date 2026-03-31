package modules.products.application

import modules.products.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend fun execute() = repository.findAll()
}