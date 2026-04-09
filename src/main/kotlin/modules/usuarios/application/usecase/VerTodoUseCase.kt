package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class VerTodoUseCase(private val repository: UsuarioRepository) {

    suspend fun execute(rolSolicitante: Int, filtroTipo: Int): List<Usuario> {

        if (rolSolicitante != 3) {
            return emptyList()
        }

        val todos = repository.verTodos()

        return when (filtroTipo) {
            1 -> todos.filter { it.idRol == 1 } // Ver Admins y SuperAdmins
            2 -> todos.filter { it.idRol == 2 } // Ver Ciudadanos
            3 -> todos.filter { it.idRol == 3 }//SuperAdmins
            else -> emptyList()
        }
    }
}