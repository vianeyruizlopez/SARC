package com.alilopez.modules.usuarios.infrastructure.persistence
import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import org.jetbrains.exposed.sql.Table

object UsuarioTable : Table("usuario") {
    val id = integer("id_usuario").autoIncrement()
    val nombre = varchar("nombre", 100)
    val primerApellido = varchar("primer_apellido", 100)
    val segundoApellido = varchar("segundo_apellido", 100)
    val email = varchar("email", 200)
    val contrasena = varchar("contrasena", 255).nullable()
    val googleId = varchar("google_id", 255).nullable()
    val edad = integer("edad")
    val idRol = integer("id_rol")

    override val primaryKey = PrimaryKey(id)
}