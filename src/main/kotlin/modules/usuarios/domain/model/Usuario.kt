package com.alilopez.modules.usuarios.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String? = null,
    val googleId: String? = null,
    val edad: Int? = null,
    val idRol: Int,
    val nombreRol: String? = null
)
