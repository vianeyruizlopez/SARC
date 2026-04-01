package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class ActualizarEstadoUseCase(private val repository: ReporteRepository) {

    suspend fun execute(idReporte: Int, nuevoEstadoId: Int): Boolean {
        if (nuevoEstadoId !in 1..3) {
            throw IllegalArgumentException("Estado no válido")
        }

        val fueActualizado = repository.actualizarEstado(idReporte, nuevoEstadoId)

        if (!fueActualizado) {
            throw IllegalArgumentException("El reporte con ID $idReporte no existe o no se pudo actualizar")
        }

        return fueActualizado
    }
}