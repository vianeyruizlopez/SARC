package com.alilopez.modules.reportes.infrastructure.persistence

import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresReporteRepository : ReporteRepository {

    override suspend fun buscarTodos(): List<Reporte> = transaction {
        reporteTable.selectAll().map { it.toDomain() }
    }

    override suspend fun buscarPorId(id: Int): Reporte? = transaction {
        reporteTable.select { reporteTable.id eq id }
            .map { it.toDomain() }
            .singleOrNull()
    }

    override suspend fun guardar(nuevoReporte: Reporte): Reporte = transaction {
        val idGenerado = reporteTable.insert {
            it[titulo] = nuevoReporte.titulo
            it[descripcion] = nuevoReporte.descripcion
            it[idUsuario] = nuevoReporte.id_usuario
            it[idIncidencia] = nuevoReporte.id_incidencia
            it[latitud] = nuevoReporte.latitud.toBigDecimal()
            it[longitud] = nuevoReporte.longitud.toBigDecimal()
            it[idEstado] = nuevoReporte.id_estado
            it[imagen] = nuevoReporte.imagen
        } get reporteTable.id

        nuevoReporte.copy(id_reporte = idGenerado)
    }

    override suspend fun actualizar(id: Int, reporte: Reporte): Reporte? = transaction {
        val filasAfectadas = reporteTable.update({ reporteTable.id eq id }) {
            it[titulo] = reporte.titulo
            it[descripcion] = reporte.descripcion
            it[idIncidencia] = reporte.id_incidencia
            it[latitud] = reporte.latitud.toBigDecimal()
            it[longitud] = reporte.longitud.toBigDecimal()
            it[idEstado] = reporte.id_estado
            it[imagen] = reporte.imagen
        }
        if (filasAfectadas > 0) reporte else null
    }

    override suspend fun actualizarEstado(id: Int, nuevoEstadoId: Int): Boolean = transaction {
        reporteTable.update({ reporteTable.id eq id }) {
            it[idEstado] = nuevoEstadoId
        } > 0
    }

    override suspend fun eliminar(id: Int): Boolean = transaction {
        reporteTable.deleteWhere { reporteTable.id eq id } > 0
    }

    override suspend fun buscarPorUsuario(idUsuario: Int): List<Reporte> = transaction {
        reporteTable.select { reporteTable.idUsuario eq idUsuario }
            .map { it.toDomain() }
    }

    private fun ResultRow.toDomain() = Reporte(
        id_reporte = this[reporteTable.id],
        id_usuario = this[reporteTable.idUsuario],
        id_incidencia = this[reporteTable.idIncidencia],
        titulo = this[reporteTable.titulo],
        descripcion = this[reporteTable.descripcion],
        latitud = this[reporteTable.latitud].toDouble(),
        longitud = this[reporteTable.longitud].toDouble(),
        id_estado = this[reporteTable.idEstado],
        imagen = this[reporteTable.imagen],
        fecha = this[reporteTable.fechaReporte]
    )
}