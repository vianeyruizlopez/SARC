package com.alilopez.modules.reportes.infrastructure.rest.dto


import com.alilopez.modules.reportes.domain.model.Reporte
import kotlinx.serialization.Serializable

@Serializable
data class ReporteResponse (
    val id_reporte: Int,
    val id_usuario: Int,
    val nombreUsuario: String,
    val nombreIncidencia: String,
    val nombreEstado: String,
    val id_incidencia: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String?,
    val latitud: Double,
    val longitud: Double,
    val id_estado: Int,
    val imagen: String?,
    val fecha: String
)

fun Reporte.toResponse() = ReporteResponse (
    id_reporte = id_reporte ?: 0,
    id_usuario = id_usuario,
    nombreUsuario = nombreUsuario ?: "Usuario Desconocido",
    id_incidencia = id_incidencia,
    titulo = titulo,
    descripcion = descripcion,
    ubicacion = ubicacion ?: "Sin ubicación",
    nombreIncidencia = nombreIncidencia ?: "Desconocida",
    nombreEstado = nombreEstado ?: "Pendiente",
    latitud = latitud,
    longitud = longitud,
    id_estado = id_estado,
    imagen = imagen,
    fecha = fecha?.toString() ?: ""
)