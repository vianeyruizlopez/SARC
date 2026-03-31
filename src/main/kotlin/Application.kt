package com.alilopez

import com.alilopez.common.infrastructure.DatabaseFactory
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import modules.products.infrastructure.productModule
import modules.products.infrastructure.rest.productRoutes
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    DatabaseFactory.init()
    // 1. Configuración de Inyección de Dependencias
    install(Koin) {
        slf4jLogger() // Opcional: para ver logs de Koin
        modules(productModule)
    }

    // 2. Configuración de Serialización (Content Negotiation)
    install(ContentNegotiation) {
        json()
    }

    // 3. Registro de Rutas
    routing {
        productRoutes()
    }
}