package com.alilopez.common.infrastructure

import com.alilopez.modules.reportes.infrastructure.persistence.reporteTable
import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import com.alilopez.modules.catalogos.infrastructure.persistence.IncidenciaTable
import com.alilopez.modules.catalogos.infrastructure.persistence.EstadoReporteTable

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"

            // Leemos del .env. Si no existe, usamos un valor por defecto (el ?: "...")
            jdbcUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/SARC-2"
            username = System.getenv("DB_USER") ?: "postgres"
            password = System.getenv("DB_PASSWORD") ?: "root"

            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                RolTable,
                IncidenciaTable,
                EstadoReporteTable,
                UsuarioTable,
                reporteTable
            )
        }
    }
}