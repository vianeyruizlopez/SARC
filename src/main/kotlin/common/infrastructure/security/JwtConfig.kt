package com.alilopez.common.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.alilopez.modules.usuarios.domain.model.Usuario
import java.util.*

object JwtConfig {
    // 1. Intentamos leer la llave del sistema (.env). Si no existe, usamos una por defecto.
    private val secret = System.getenv("JWT_SECRET") ?: "llave_temporal_muy_larga_123"
    private const val issuer = "com.alilopez.sarc" // Quien emite el token
    private val algorithm = Algorithm.HMAC256(secret) // El algoritmo de cifrado

    // 2. Este "verifier" lo usa Ktor para checar si un token que llega es real
    val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    // 3. Esta función crea el token cuando alguien hace Login
    fun generateToken(usuario: Usuario): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        // Guardamos el ID y el ROL dentro del token para no ir a la DB cada segundo
        .withClaim("id", usuario.id)
        .withClaim("idRol", usuario.idRol)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000 * 24)) // Expira en 24h
        .sign(algorithm) // Lo firmamos con nuestra llave secreta
}