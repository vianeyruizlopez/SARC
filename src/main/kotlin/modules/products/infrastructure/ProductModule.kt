package modules.products.infrastructure

import com.alilopez.modules.products.application.DeleteProductUseCase
import com.alilopez.modules.products.application.UpdateProductUseCase
import modules.products.application.CreateProductUseCase
import modules.products.application.GetProductsUseCase
import modules.products.domain.repository.ProductRepository
import modules.products.infrastructure.persistence.InMemoryProductRepository
import modules.products.infrastructure.persistence.PostgresProductRepository
import org.koin.dsl.module

val productModule = module {
    //single<ProductRepository> { InMemoryProductRepository() }
    factory { CreateProductUseCase(get()) }
    factory { GetProductsUseCase(get()) }
    factory { UpdateProductUseCase(get()) } // NUEVO
    factory { DeleteProductUseCase(get()) } // NUEVO
    single<ProductRepository> { PostgresProductRepository() }
}