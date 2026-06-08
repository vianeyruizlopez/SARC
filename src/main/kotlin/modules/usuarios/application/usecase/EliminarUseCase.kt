package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class EliminarUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(id: Int): Boolean {
        return repository.eliminar(id)
    }
}