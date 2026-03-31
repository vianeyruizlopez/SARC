package com.alilopez.modules.catalogos.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object IncidenciaTable : Table("cat_incidencias") {
    val id = integer("id_insidencias").autoIncrement()
    val nombre = varchar("nombre", 100)

    override val primaryKey = PrimaryKey(id)
}