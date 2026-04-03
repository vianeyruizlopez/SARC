package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class VerReportesPorUsuarioUseCase(private val repository: ReporteRepository) {

    suspend fun execute(idUsuario: Int, rol: Int, estadoFiltro: Int?): List<Reporte> {
        if (rol != 2) {
            throw IllegalAccessException("No tienes permiso para ver estos reportes.")
        }

        return repository.buscarPorUsuario(idUsuario, estadoFiltro)
    }
}