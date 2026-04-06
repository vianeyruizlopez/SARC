package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class CrearReporteUseCase(private val repository: ReporteRepository) {

    suspend fun execute(nuevoReporte: Reporte): Reporte {
        val reportesExistentes = repository.buscarTodos(null, null)

        val duplicado = reportesExistentes.find { r ->
            r.latitud == nuevoReporte.latitud &&
                    r.longitud == nuevoReporte.longitud &&
                    (r.id_estado == 1 || r.id_estado == 2)
        }

        if (duplicado != null) {
            throw IllegalStateException("Ya existe un reporte activo en esta ubicación.")
        }
        return repository.guardar(nuevoReporte)
    }
}