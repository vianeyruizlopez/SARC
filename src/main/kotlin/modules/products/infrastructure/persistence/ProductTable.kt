package com.alilopez.modules.products.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object ProductTable : Table("products") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val description = text("description")
    val price = double("price")
    val stock = integer("stock")

    override val primaryKey = PrimaryKey(id)
}