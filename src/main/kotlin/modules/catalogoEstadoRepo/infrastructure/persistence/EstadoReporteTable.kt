package com.alilopez.modules.catalogos.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object EstadoReporteTable : Table("cat_estado_reporte") {
    val id = integer("id_estado").autoIncrement()
    val nombre = varchar("nombre", 100)

    override val primaryKey = PrimaryKey(id)
}