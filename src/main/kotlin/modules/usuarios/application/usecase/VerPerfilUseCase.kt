package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class VerPerfilUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(id: Int): Usuario? {
        return repository.verPorId(id)
    }
}