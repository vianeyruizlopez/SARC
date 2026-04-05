package com.alilopez.modules.reportes.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReporteMapaResponse(
    val idReporte: Int,
    val latitud: Double,
    val longitud: Double,
    val idIncidencia: Int,
    val idEstado: Int,
    val titulo: String
)