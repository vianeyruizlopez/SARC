package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteEstadisticasResponse

class VerEstadisticasGlobalesUseCase(private val repository: ReporteRepository) {
    suspend fun execute(rol: Int): ReporteEstadisticasResponse {
        if (rol != 1 && rol != 3) {
            throw IllegalAccessException("No tienes permisos para ver estadísticas globales")
        }
        return repository.obtenerEstadisticasGlobales()
    }
}