package com.alilopez.modules.products.infrastructure.rest.dto

import com.alilopez.common.infrastructure.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import modules.products.domain.model.Product
import java.util.UUID

@Serializable
data class ProductResponse(
    @Serializable(with = UUIDSerializer::class) // Usamos el serializer de arriba
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
)

// Extension function para mapear de Dominio a DTO
fun Product.toResponse() = ProductResponse(
    id = id ?: UUID.randomUUID(), // O manejar el nulo según tu lógica
    name = name,
    description = description,
    price = price,
    stock = stock
)