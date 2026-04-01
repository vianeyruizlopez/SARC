package com.alilopez.modules.reportes.domain.model
import java.time.LocalDateTime


data class Reporte(
    val id_reporte: Int? = null,
    val id_usuario: Int,
    val id_incidencia: Int,
    val titulo: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val id_estado: Int,
    val imagen: String? = null,
    val fecha: java.time.LocalDateTime? = null
)
