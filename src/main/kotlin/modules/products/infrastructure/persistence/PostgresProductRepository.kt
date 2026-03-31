package modules.products.infrastructure.persistence

import com.alilopez.modules.products.infrastructure.persistence.ProductTable
import modules.products.domain.model.Product
import modules.products.domain.repository.ProductRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class PostgresProductRepository : ProductRepository {

    override suspend fun findById(id: UUID): Product? = transaction {
        ProductTable.select { ProductTable.id eq id }
            .map { it.toDomain() }
            .singleOrNull()
    }

    override suspend fun findByName(name: String): Product? = transaction {
        ProductTable.select { ProductTable.name eq name }
            .map { it.toDomain() }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Product> = transaction {
        ProductTable.selectAll().map { it.toDomain() }
    }

    override suspend fun save(product: Product): Product {
        val newId = product.id ?: UUID.randomUUID()
        transaction {
            ProductTable.insert {
                it[id] = newId
                it[name] = product.name
                it[description] = product.description
                it[price] = product.price
                it[stock] = product.stock
            }
        }
        return product.copy(id = newId)
    }

    override suspend fun update(product: Product): Product? {
        val id = product.id ?: return null
        return transaction {
            val updatedRows = ProductTable.update({ ProductTable.id eq id }) {
                it[name] = product.name
                it[description] = product.description
                it[price] = product.price
                it[stock] = product.stock
            }
            if (updatedRows > 0) product else null
        }
    }

    override suspend fun delete(id: UUID): Boolean = transaction {
        val deletedRows = ProductTable.deleteWhere { ProductTable.id eq id }
        deletedRows > 0
    }

    // Mapper: Convierte una fila de la DB a tu Entidad de Dominio
    private fun ResultRow.toDomain() = Product(
        id = this[ProductTable.id],
        name = this[ProductTable.name],
        description = this[ProductTable.description],
        price = this[ProductTable.price],
        stock = this[ProductTable.stock]
    )
}