package com.alilopez.modules.usuarios.infrastructure.rest.dto

import com.alilopez.modules.usuarios.domain.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioResponse(
    val id: Int?,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String?,
    val googleId: String?,
    val edad: Int,
    val idRol: Int,
    val nombreRol: String
)

fun Usuario.toResponse() = UsuarioResponse(
    id = id ?: 0,
    nombre = nombre,
    primerApellido = primerApellido,
    segundoApellido = segundoApellido,
    email = email,
    contrasena = null,
    googleId = googleId,
    edad = edad,
    idRol = idRol,
    nombreRol = this.nombreRol ?: "Sin Rol"
)