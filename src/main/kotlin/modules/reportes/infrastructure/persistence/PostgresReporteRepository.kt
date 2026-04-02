package com.alilopez.modules.reportes.infrastructure.persistence

import com.alilopez.modules.catalogos.infrastructure.persistence.EstadoReporteTable
import com.alilopez.modules.catalogos.infrastructure.persistence.IncidenciaTable
import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.domain.repository.ReporteRepository
import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresReporteRepository : ReporteRepository {

    private val reporteConNombres = reporteTable
        .innerJoin(IncidenciaTable, { reporteTable.idIncidencia }, { IncidenciaTable.id })
        .innerJoin(EstadoReporteTable, { reporteTable.idEstado }, { EstadoReporteTable.id })
        .innerJoin(UsuarioTable, { reporteTable.idUsuario }, { UsuarioTable.id })

    override suspend fun buscarTodos(): List<Reporte> = transaction {
        reporteConNombres.selectAll().map { it.toDomain() }
    }

    override suspend fun buscarPorId(id: Int): Reporte? = transaction {
        reporteConNombres.select { reporteTable.id eq id }
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
            it[ubicacion] = nuevoReporte.ubicacion
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
            it[ubicacion] = reporte.ubicacion
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
        reporteConNombres.select { reporteTable.idUsuario eq idUsuario }
            .map { it.toDomain() }
    }

    private fun ResultRow.toDomain() = Reporte(
        id_reporte = this[reporteTable.id],
        id_usuario = this[reporteTable.idUsuario],
        nombreUsuario = this[UsuarioTable.nombre],
        id_incidencia = this[reporteTable.idIncidencia],
        nombreIncidencia = this[IncidenciaTable.nombre],
        titulo = this[reporteTable.titulo],
        descripcion = this[reporteTable.descripcion],
        ubicacion = this[reporteTable.ubicacion],
        latitud = this[reporteTable.latitud].toDouble(),
        longitud = this[reporteTable.longitud].toDouble(),
        id_estado = this[reporteTable.idEstado],
        nombreEstado = this[EstadoReporteTable.nombre],
        imagen = this[reporteTable.imagen],
        fecha = this[reporteTable.fechaReporte]
    )
}