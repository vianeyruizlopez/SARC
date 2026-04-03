package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class VerTodoUseCase(private val repository: UsuarioRepository) {

    // filtroTipo: 1 para Staff, 2 para Ciudadanos
    suspend fun execute(rolSolicitante: Int, filtroTipo: Int): List<Usuario> {

        // REGLA TAJANTE: Si el que pide NO es SuperAdmin (3), devolvemos lista vacía de inmediato
        if (rolSolicitante != 3) {
            return emptyList()
        }

        // Si llegó aquí, es porque es Rol 3. Ahora traemos los datos.
        val todos = repository.verTodos()

        return when (filtroTipo) {
            1 -> todos.filter { it.idRol == 1 || it.idRol == 3 } // Ver Admins y SuperAdmins
            2 -> todos.filter { it.idRol == 2 } // Ver Ciudadanos
            else -> emptyList()
        }
    }
}