package com.alilopez.modules.reportes.domain.model
import java.time.LocalDateTime


data class reporte(
    val id: Int?=null,
    val titulo: String,
    val descripcion: String,
    val imagen : String,
    val latitud: Double,
    val longitud: Double,
    val fechaReporte: LocalDateTime,
    val idEstado: Int,
    val idIncidencia: Int,
    val idUsuario: Int
)
