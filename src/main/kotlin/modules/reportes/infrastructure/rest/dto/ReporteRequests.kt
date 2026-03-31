package com.alilopez.modules.reportes.infrastructure.rest.dto

import com.alilopez.modules.reportes.domain.model.reporte
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateReporteRequest(
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val latitud: Double,
    val longitud: Double,
    val idIncidencia: Int,
    val idUsuario: Int
) {
    fun toDomain() = reporte(
        id = null,
        titulo = titulo,
        descripcion = descripcion,
        imagen = imagen,
        latitud = latitud,
        longitud = longitud,
        fechaReporte = LocalDateTime.now(),
        idEstado = 1,
        idIncidencia = idIncidencia,
        idUsuario = idUsuario
    )
}