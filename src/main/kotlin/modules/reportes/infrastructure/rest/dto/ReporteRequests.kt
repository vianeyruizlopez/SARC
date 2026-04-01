package com.alilopez.modules.reportes.infrastructure.rest.dto

import com.alilopez.modules.reportes.domain.model.Reporte
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateReporteRequest(
    val id_usuario: Int,
    val id_incidencia: Int,
    val titulo: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val id_estado: Int = 1,
    val imagen: String? = null
) {
    fun toDomain() = Reporte(
        id_usuario = id_usuario,
        id_incidencia = id_incidencia,
        titulo = titulo,
        descripcion = descripcion,
        latitud = latitud,
        longitud = longitud,
        id_estado = id_estado,
        imagen = imagen
    )
}