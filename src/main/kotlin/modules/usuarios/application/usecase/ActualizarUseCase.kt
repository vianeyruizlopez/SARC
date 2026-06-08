package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class ActualizarUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(id: Int,usuario: Usuario): Usuario {
        val actualizarUsuario = usuario.copy(id = id)
        return repository.actualizar(id, actualizarUsuario)
            ?:throw IllegalArgumentException("El reporte con ID $id no fue encontrado.")
    }
}