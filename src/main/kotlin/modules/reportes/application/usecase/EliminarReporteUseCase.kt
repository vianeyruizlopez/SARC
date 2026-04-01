package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class EliminarReporteUseCase (private val repository: ReporteRepository){
    suspend fun execute(id: Int) {
        val fueEliminado = repository.eliminar(id)
        if (!fueEliminado) {
            throw IllegalArgumentException("No se pudo eliminar: El reporte con ID $id no existe.")
        }
    }
}