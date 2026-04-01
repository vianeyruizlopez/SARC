package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class VerReportesPorUsuarioUseCase(private val repository: ReporteRepository) {

    suspend fun execute(idUsuario: Int): List<Reporte> {
        return repository.buscarPorUsuario(idUsuario)
    }
}