package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteEstadisticasResponse

class VerEstadisticasUsuarioUseCase(private val repository: ReporteRepository) {
    suspend fun execute(idUsuario: Int, rol: Int): ReporteEstadisticasResponse {
        if (rol != 2) throw IllegalAccessException("Solo ciudadanos pueden ver sus estadísticas")
        return repository.obtenerEstadisticasUsuario(idUsuario)
    }
}