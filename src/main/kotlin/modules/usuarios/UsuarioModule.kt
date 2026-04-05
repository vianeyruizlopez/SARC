package com.alilopez.modules.usuarios

import LoginUseCase
import com.alilopez.modules.usuarios.application.usecase.ActualizarUseCase
import com.alilopez.modules.usuarios.application.usecase.EliminarUseCase
import com.alilopez.modules.usuarios.application.usecase.RegistrarAdminUseCase
import com.alilopez.modules.usuarios.application.usecase.RegistrarUseCase
import com.alilopez.modules.usuarios.application.usecase.VerEstadisticasUsuariosUseCase
import com.alilopez.modules.usuarios.application.usecase.VerPerfilUseCase
import com.alilopez.modules.usuarios.application.usecase.VerTodoUseCase
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import com.alilopez.modules.usuarios.infrastructure.persistence.PostgresUsuarioRepository
import org.koin.dsl.module

val usuarioModule = module {
    factory { LoginUseCase(get(), get ()) }
    factory { ActualizarUseCase(get())}
    factory { EliminarUseCase(get()) }
    factory { RegistrarUseCase(get()) }
    factory { VerPerfilUseCase(get()) }
    factory { VerTodoUseCase(get()) }
    factory { RegistrarAdminUseCase(get()) }
    factory { VerEstadisticasUsuariosUseCase(get()) }
    single<UsuarioRepository> { PostgresUsuarioRepository() }
}