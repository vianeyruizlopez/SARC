package com.alilopez.modules.reportes.infrastructure.persistence

import com.alilopez.modules.catalogos.infrastructure.persistence.EstadoReporteTable
import com.alilopez.modules.catalogos.infrastructure.persistence.IncidenciaTable
import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object reporteTable : Table("reporte") {
    val id = integer("id_reporte").autoIncrement()
    val titulo = varchar("titulo", 250)
    val descripcion = varchar("descripcion", 1000)
    val imagen = varchar("imagen", 500).nullable() // Es la URL de Cloudinary
    val latitud = decimal("latitud", 10, 8)
    val longitud = decimal("longitud", 11, 8)
    val fechaReporte = datetime("fecha_reporte").clientDefault { LocalDateTime.now() }
    val idUsuario = integer("id_usuario").references(UsuarioTable.id)
    val idIncidencia = integer("id_incidencia").references(IncidenciaTable.id)
    val idEstado = integer("id_estado").references(EstadoReporteTable.id).default(1)
    val ubicacion = varchar("ubicacion", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}