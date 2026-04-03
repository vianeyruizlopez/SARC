package com.alilopez.modules.usuarios.infrastructure.rest

import LoginUseCase
import com.alilopez.modules.usuarios.application.usecase.*
import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.infrastructure.rest.dto.toResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.usuarioRouting() {
    val actualizarUseCase by inject<ActualizarUseCase>()
    val eliminarUseCase by inject<EliminarUseCase>()
    val loginUseCase by inject<LoginUseCase>()
    val registrarAdminUseCase by inject<RegistrarAdminUseCase>()
    val registrarUseCase by inject<RegistrarUseCase>()
    val verPerfilUseCase by inject <VerPerfilUseCase>()
    val verTodoUseCase by inject<VerTodoUseCase>()
    route("/auth") {

        post("/google") {
            val request = call.receive<GoogleLoginRequest>()
            val token = loginUseCase.execute(
                email = "ejemplo@gmail.com", // TODO: Cambiar por email real de Firebase
                googleId = request.idToken,
                nombre = "Usuario Google"
            )
            call.respond(mapOf("token" to token))
        }

        post("/register") {
            val datosCliente = call.receive<Usuario>()
            val clienteParaRegistrar = datosCliente.copy(idRol = 2)
            val resultado = registrarUseCase.execute(clienteParaRegistrar)

            if (resultado != null) {
                call.respond(HttpStatusCode.Created, resultado.toResponse())
            } else {
                call.respond(HttpStatusCode.BadRequest, "No se pudo crear la cuenta")
            }
        }
    }

    authenticate("auth-jwt") {
        route("/usuarios") {

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")

                val usuario = verPerfilUseCase.execute(id)

                if (usuario != null) {
                    call.respond(HttpStatusCode.OK, usuario.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            }

            post("/admin") {
                val principal = call.principal<JWTPrincipal>()
                val rolEjecutor = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val nuevoAdminData = call.receive<Usuario>()
                val resultado = registrarAdminUseCase.execute(nuevoAdminData, rolEjecutor)

                if (resultado != null) {
                    call.respond(HttpStatusCode.Created, resultado.toResponse())
                } else {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permisos de SuperAdmin"))
                }
            }

            get("/staff") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (rol == 3) {
                    val lista = verTodoUseCase.execute(rol, filtroTipo = 1)
                    call.respond(lista.map { it.toResponse() })
                } else {
                    call.respond(HttpStatusCode.Forbidden, "No tienes permiso para ver el Staff")
                }
            }

            get("/ciudadanos") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (rol == 3) {
                    val lista = verTodoUseCase.execute(rol, filtroTipo = 2)
                    call.respond(lista.map { it.toResponse() })
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Acceso denegado")
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val usuarioData = call.receive<Usuario>()

                val resultado = actualizarUseCase.execute(id, usuarioData)

                if (resultado != null) {
                    call.respond(HttpStatusCode.OK, resultado.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val rolEjecutor = principal?.payload?.getClaim("idRol")?.asInt() ?: 0
                val idAEliminar = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)

                if (rolEjecutor == 3) {
                    val eliminado = eliminarUseCase.execute(idAEliminar)
                    if (eliminado) {
                        call.respond(HttpStatusCode.OK, "Usuario eliminado correctamente")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "No se pudo eliminar")
                    }
                } else {
                    call.respond(HttpStatusCode.Forbidden, "No tienes permisos para eliminar")
                }
            }
        }
    }
}

@Serializable
data class GoogleLoginRequest(val idToken: String)