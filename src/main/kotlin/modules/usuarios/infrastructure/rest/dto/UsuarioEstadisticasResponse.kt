package com.alilopez.modules.usuarios.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioEstadisticasResponse(
    val totalUsuarios: Long,
    val administradores: Long,
    val ciudadanos: Long
)