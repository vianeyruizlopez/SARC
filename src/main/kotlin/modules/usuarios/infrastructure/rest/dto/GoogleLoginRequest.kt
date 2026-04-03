package com.alilopez.modules.usuarios.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val idToken: String,
    val email: String,
    val nombre: String
)