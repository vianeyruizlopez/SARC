package com.alilopez.modules.reportes.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReporteEstadisticasGlobalResponse(
    val total: Long,
    val pendientes: Long,
    val enProceso: Long,
    val resueltos: Long,
    val activos: Long,
    val porCategoria: List<CategoriaEstadistica>
)

@Serializable
data class CategoriaEstadistica(
    val nombre: String,
    val cantidad: Long
)