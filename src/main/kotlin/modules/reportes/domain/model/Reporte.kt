package com.alilopez.modules.reportes.domain.model
import java.time.LocalDateTime


data class Reporte(
    val id_reporte: Int? = null,
    val id_usuario: Int,
    val nombreUsuario: String? = null,
    val id_incidencia: Int,
    val nombreIncidencia: String? = null,
    val titulo: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val ubicacion: String? = null,
    val id_estado: Int,
    val nombreEstado: String? = null,
    val imagen: String? = null,
    val fecha: java.time.LocalDateTime? = null
)
