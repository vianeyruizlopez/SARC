package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class VerReporteUseCase(private val repository: ReporteRepository) {
    suspend fun execute() = repository.buscarTodos()
}