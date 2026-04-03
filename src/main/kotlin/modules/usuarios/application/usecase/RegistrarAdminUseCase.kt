package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class RegistrarAdminUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(nuevoAdmin: Usuario, idRolEjecutor: Int): Usuario? {

        if (idRolEjecutor != 3) {
            return null
        }
        val usuarioParaGuardar = nuevoAdmin.copy(idRol = 1)

        return repository.registrar(usuarioParaGuardar)
    }
}