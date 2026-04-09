package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import com.alilopez.modules.usuarios.infrastructure.rest.dto.UsuarioEstadisticasResponse

class VerEstadisticasUsuariosUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(rol: Int): UsuarioEstadisticasResponse {
        println("DEBUG: El UseCase recibió el rol: $rol")
        if (rol != 3) {
            throw IllegalAccessException("Acceso restringido...")
        }
        return repository.obtenerEstadisticasUsuarios()
    }
}