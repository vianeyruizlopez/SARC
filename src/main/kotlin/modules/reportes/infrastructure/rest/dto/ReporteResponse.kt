package com.alilopez.modules.reportes.infrastructure.rest.dto


import com.alilopez.modules.reportes.domain.model.Reporte
import kotlinx.serialization.Serializable

@Serializable
data class ReporteResponse (
    val id_reporte: Int,
    val id_usuario: Int,
    val id_incidencia: Int,
    val titulo: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val id_estado: Int,
    val imagen: String?,
    val fecha: String
)

fun Reporte.toResponse() = ReporteResponse (
    id_reporte = id_reporte ?: 0,
    id_usuario = id_usuario,
    id_incidencia = id_incidencia,
    titulo = titulo,
    descripcion = descripcion,
    latitud = latitud,
    longitud = longitud,
    id_estado = id_estado,
    imagen = imagen,
    fecha = fecha?.toString() ?: ""
)