package com.alilopez.modules.usuarios.infrastructure.persistence

import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PostgresUsuarioRepository : UsuarioRepository {

    private fun UsuariosConRoles() = UsuarioTable.join(
        RolTable,
        joinType = JoinType.INNER,
        onColumn = UsuarioTable.idRol,
        otherColumn = RolTable.id
    )

    override suspend fun verTodos(): List<Usuario> = newSuspendedTransaction {
        UsuariosConRoles()
            .selectAll()
            .map { toDomainConRol(it) }
    }



    override suspend fun verPorId(id: Int): Usuario? = newSuspendedTransaction {
        UsuariosConRoles()
            .select { UsuarioTable.id eq id }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }

    override suspend fun verPorEmail(email: String): Usuario? = newSuspendedTransaction {
        UsuariosConRoles()
            .select { UsuarioTable.email eq email }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }
    override suspend fun registrar(usuario: Usuario): Usuario? = newSuspendedTransaction {
        val nuevoId = UsuarioTable.insert {
            it[nombre] = usuario.nombre
            it[primerApellido] = usuario.primerApellido
            it[segundoApellido] = usuario.segundoApellido
            it[email] = usuario.email
            it[edad] = usuario.edad
            it[contrasena] = usuario.contrasena
            it[googleId] = usuario.googleId
            it[idRol] = usuario.idRol
        }[UsuarioTable.id]

        UsuariosConRoles()
            .select { UsuarioTable.id eq nuevoId }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }

    override suspend fun actualizar(id: Int, usuario: Usuario): Usuario? = newSuspendedTransaction {
        val filasAfectadas = UsuarioTable.update({ UsuarioTable.id eq id }) {
            it[nombre] = usuario.nombre
            it[primerApellido] = usuario.primerApellido
            it[segundoApellido] = usuario.segundoApellido
            it[edad] = usuario.edad
        }
        if (filasAfectadas > 0) verPorId(id) else null
    }

    override suspend fun eliminar(id: Int): Boolean = newSuspendedTransaction {
        UsuarioTable.deleteWhere { UsuarioTable.id eq id } > 0
    }

    private fun toDomainConRol(row: ResultRow): Usuario = Usuario(
        id = row[UsuarioTable.id],
        nombre = row[UsuarioTable.nombre],
        primerApellido = row[UsuarioTable.primerApellido],
        segundoApellido = row[UsuarioTable.segundoApellido],
        email = row[UsuarioTable.email],
        edad = row[UsuarioTable.edad],
        googleId = row[UsuarioTable.googleId],
        contrasena = row[UsuarioTable.contrasena],
        idRol = row[UsuarioTable.idRol],
        nombreRol = row[RolTable.nombre]
    )
}