package com.alilopez.common.infrastructure.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") { // Nombre de nuestro filtro de seguridad
            verifier(JwtConfig.verifier) // Usamos el verificador que creamos arriba
            validate { credential ->
                val id = credential.payload.getClaim("id").asInt()
                val rol = credential.payload.getClaim("idRol").asInt()

                if (id != null && rol != null) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}