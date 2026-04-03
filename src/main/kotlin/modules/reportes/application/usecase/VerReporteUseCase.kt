package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class VerReporteUseCase(private val repository: ReporteRepository) {

    suspend fun execute(rol: Int, idEstado: Int?): List<Reporte> {
        if (rol != 1 && rol != 3) {
            throw IllegalAccessException("Solo el personal administrativo puede consultar todos los reportes.")
        }

        return repository.buscarTodos(idEstado)
    }
}