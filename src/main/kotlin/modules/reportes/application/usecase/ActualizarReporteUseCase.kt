package com.alilopez.modules.reportes.application.usecase

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository

class ActualizarReporteUseCase(private val repository: ReporteRepository) {
    suspend fun execute(id: Int, datosNuevos: Reporte): Reporte? {
        // 1. Usamos 'buscarPorId' (como dice tu interfaz)
        val reporteActual = repository.buscarPorId(id) ?: return null

        // 2. Lógica de mezcla
        val reporteMezclado = reporteActual.copy(
            titulo = if (datosNuevos.titulo.isNotEmpty()) datosNuevos.titulo else reporteActual.titulo,
            descripcion = if (datosNuevos.descripcion.isNotEmpty()) datosNuevos.descripcion else reporteActual.descripcion,
            id_incidencia = if (datosNuevos.id_incidencia != 0) datosNuevos.id_incidencia else reporteActual.id_incidencia,
            latitud = if (datosNuevos.latitud != 0.0) datosNuevos.latitud else reporteActual.latitud,
            longitud = if (datosNuevos.longitud != 0.0) datosNuevos.longitud else reporteActual.longitud,
            id_estado = if (datosNuevos.id_estado != 0) datosNuevos.id_estado else reporteActual.id_estado,
            imagen = datosNuevos.imagen ?: reporteActual.imagen
        )

        return repository.actualizar(id, reporteMezclado)
    }
}