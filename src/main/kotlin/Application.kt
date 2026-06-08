package com.alilopez

import com.alilopez.common.infrastructure.DatabaseFactory
import com.alilopez.common.infrastructure.security.configureSecurity
import com.alilopez.modules.reportes.infrastructure.rest.reporteRouting
import com.alilopez.modules.reportes.reporteModule
import com.alilopez.modules.usuarios.infrastructure.rest.usuarioRouting
import com.alilopez.modules.usuarios.usuarioModule
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import modules.products.infrastructure.productModule
import modules.products.infrastructure.rest.productRoutes
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import io.ktor.server.plugins.cors.routing.*
fun Application.module() {
    DatabaseFactory.init()
    // 1. Configuración de Inyección de Dependencias

    configureSecurity()

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowNonSimpleContentTypes = true
        allowCredentials = true
    }

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