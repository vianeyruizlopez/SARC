package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteMapaResponse

class VerReportesMapaUseCase(private val repository: ReporteRepository) {
    suspend fun execute(rol: Int, idEstado: Int?, idIncidencia: Int?): List<ReporteMapaResponse> {
        if (rol != 1 && rol != 2) {
            throw IllegalAccessException("No tienes permisos para visualizar el mapa interactivo.")
        }
        return repository.obtenerReportesParaMapa(idEstado, idIncidencia)
    }
}