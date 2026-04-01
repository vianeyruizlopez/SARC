package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class VerIdReporteUseCase (private val repository: ReporteRepository) {
    suspend fun execute(id: Int): Reporte? {
        return repository.buscarPorId(id)
    }
}