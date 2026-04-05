package com.alilopez.modules.reportes.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReporteEstadisticasResponse(
    val total: Long,
    val pendientes: Long,
    val enProceso: Long,
    val resueltos: Long
)