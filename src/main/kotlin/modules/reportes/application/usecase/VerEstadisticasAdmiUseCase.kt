package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteEstadisticasGlobalResponse

class VerEstadisticasAdmiUseCase(private val repository: ReporteRepository) {

    suspend fun execute(rol: Int): ReporteEstadisticasGlobalResponse {
        if (rol != 3) {
            throw IllegalAccessException("No tienes permisos para ver las estadísticas globales")
        }

        return repository.obtenerEstadisticasCompletas()
    }
}