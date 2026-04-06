package com.alilopez.modules.reportes.domain.repository
import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteEstadisticasGlobalResponse
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteEstadisticasResponse
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteMapaResponse
import com.alilopez.modules.reportes.infrastructure.rest.dto.ReporteResponse

interface ReporteRepository {
    suspend fun buscarPorId(id: Int): Reporte?
    suspend fun buscarTodos(idEstado: Int?): List<Reporte>
    suspend fun guardar(reporte: Reporte): Reporte
    suspend fun actualizar(id: Int, reporte: Reporte): Reporte?
    suspend fun eliminar(id: Int): Boolean
    suspend fun buscarPorUsuario(idUsuario: Int, idEstado: Int?): List<Reporte>
    suspend fun actualizarEstado(id: Int, nuevoEstadoId: Int): Boolean
    suspend fun obtenerEstadisticasUsuario(idUsuario: Int): ReporteEstadisticasResponse
    suspend fun obtenerEstadisticasGlobales(): ReporteEstadisticasResponse
    suspend fun obtenerReportesParaMapa(idEstado: Int?, idIncidencia: Int?): List<ReporteMapaResponse>
    suspend fun obtenerReportesDetallados(idEstado: Int?, idIncidencia: Int?): List<ReporteResponse>
    suspend fun obtenerEstadisticasCompletas(): ReporteEstadisticasGlobalResponse
}