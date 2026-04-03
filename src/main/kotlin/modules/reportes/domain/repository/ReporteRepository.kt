package com.alilopez.modules.reportes.domain.repository
import com.alilopez.modules.reportes.domain.model.Reporte

interface ReporteRepository {
    suspend fun buscarPorId(id: Int): Reporte?
    suspend fun buscarTodos(idEstado: Int?): List<Reporte>
    suspend fun guardar(reporte: Reporte): Reporte
    suspend fun actualizar(id: Int, reporte: Reporte): Reporte?
    suspend fun eliminar(id: Int): Boolean
    suspend fun buscarPorUsuario(idUsuario: Int, idEstado: Int?): List<Reporte>
    suspend fun actualizarEstado(id: Int, nuevoEstadoId: Int): Boolean
}