package com.alilopez

import com.alilopez.common.infrastructure.DatabaseFactory
import com.alilopez.common.infrastructure.security.configureSecurity
import com.alilopez.modules.reportes.infrastructure.rest.reporteRouting
import com.alilopez.modules.reportes.reporteModule
import com.alilopez.modules.usuarios.infrastructure.rest.usuarioRouting
import com.alilopez.modules.usuarios.usuarioModule
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.netty.util.internal.InternalThreadLocalMap.get
import modules.products.infrastructure.productModule
import modules.products.infrastructure.rest.productRoutes
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.ktor.ext.get

fun Application.module() {
    DatabaseFactory.init()
    // 1. Configuración de Inyección de Dependencias

    configureSecurity()

    install(Koin) {
        slf4jLogger() // Opcional: para ver logs de Koin
        modules(productModule, reporteModule, usuarioModule)
    }

    // 2. Configuración de Serialización (Content Negotiation)
    install(ContentNegotiation) {
        json()
    }

    // 3. Registro de Rutas
    routing {
        productRoutes()
        reporteRouting()
        usuarioRouting()

    }
}