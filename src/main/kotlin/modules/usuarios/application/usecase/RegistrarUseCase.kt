package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class RegistrarUseCase(private val repository: UsuarioRepository) {

    suspend fun execute(usuario: Usuario): Usuario? {
        val usuarioCiudadano = usuario.copy(idRol = 2)
        val existe = repository.verPorEmail(usuarioCiudadano.email)

        return if (existe == null) {
            repository.registrar(usuarioCiudadano)
        } else {
            existe
        }
    }
}