package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteResponse

class VerListaReportesAdminUseCase(private val repository: ReporteRepository) {
    suspend fun execute(rol: Int, idEstado: Int?, idIncidencia: Int?): List<ReporteResponse> {
        if (rol != 1 ) {
            throw IllegalAccessException("No tienes permisos para ver la lista global de reportes.")
        }

        return repository.obtenerReportesDetallados(idEstado, idIncidencia)
    }
}