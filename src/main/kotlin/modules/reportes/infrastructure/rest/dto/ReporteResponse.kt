package com.alilopez.modules.reportes.infrastructure.rest.dto


import com.alilopez.modules.reportes.domain.model.reporte
import kotlinx.serialization.Serializable

@Serializable
data class ReporteResponse (
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val latitud: Double,
    val longitud: Double,
    val fechaReporte: String,
    val idEstado: Int,
    val idIncidencia: Int,
    val idUsuario: Int
)

fun reporte.toResponse() = ReporteResponse (
    id = id ?: 0,
    titulo = titulo,
    descripcion = descripcion,
    imagen = imagen,
    latitud = latitud,
    longitud = longitud,
    fechaReporte = fechaReporte.toString(),
    idEstado = idEstado,
    idIncidencia = idIncidencia,
    idUsuario = idUsuario
)