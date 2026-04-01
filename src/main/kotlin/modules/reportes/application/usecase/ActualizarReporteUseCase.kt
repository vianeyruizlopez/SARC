package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class ActualizarReporteUseCase (private val repository: ReporteRepository){
    suspend fun execute(id: Int, reporteData: Reporte): Reporte {
        val reporteToUpdate = reporteData.copy(id_reporte = id)
        return repository.actualizar(id, reporteToUpdate)
            ?: throw IllegalArgumentException("El reporte con ID $id no fue encontrado.")
    }
}