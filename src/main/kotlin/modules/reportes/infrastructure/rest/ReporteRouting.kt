package com.alilopez.modules.reportes.infrastructure.rest

import com.alilopez.modules.reportes.application.usecase.*
import com.alilopez.modules.reportes.infrastructure.rest.dto.CreateReporteRequest
import com.alilopez.modules.reportes.infrastructure.rest.dto.toResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reporteRouting() {
    val crearReporteUseCase by inject<CrearReporteUseCase>()
    val verReporteUseCase by inject<VerReporteUseCase>()
    val verIdReporteUseCase by inject<VerIdReporteUseCase>()
    val actualizarReporteUseCase by inject<ActualizarReporteUseCase>()
    val eliminarReporteUseCase by inject<EliminarReporteUseCase>()
    val verPorUsuarioUseCase by inject<VerReportesPorUsuarioUseCase>()
    val actualizarEstadoUseCase by inject<ActualizarEstadoUseCase>()

    route("/reporte") {
        get {
            val reportes = verReporteUseCase.execute()
            call.respond(HttpStatusCode.OK, reportes.map { it.toResponse() })
        }

        post {
            val request = call.receive<CreateReporteRequest>()
            val nuevo = crearReporteUseCase.execute(request.toDomain())
            call.respond(HttpStatusCode.Created, nuevo.toResponse())
        }

        get("/usuario/{idUsuario}") {
            val id = call.parameters["idUsuario"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                return@get
            }
            val misReportes = verPorUsuarioUseCase.execute(id)
            call.respond(HttpStatusCode.OK, misReportes.map { it.toResponse() })
        }

        route("/{id}") {

            get {
                val id = call.parameters["id"]?.toIntOrNull()
                val reporte = id?.let { verIdReporteUseCase.execute(it) }
                if (reporte != null) {
                    call.respond(HttpStatusCode.OK, reporte.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Reporte no encontrado")
                }
            }

            put {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@put
                }
                val request = call.receive<CreateReporteRequest>()
                val resultado = actualizarReporteUseCase.execute(id, request.toDomain())
                if (resultado != null) {
                    call.respond(HttpStatusCode.OK, resultado.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound, "No se encontró el reporte")
                }
            }
            delete {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    eliminarReporteUseCase.execute(id)
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                }
            }

            patch("/estado") {
                val id = call.parameters["id"]?.toIntOrNull()
                val nuevoEstadoId = call.receive<Map<String, Int>>()["id_estado"]

                if (id == null || nuevoEstadoId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID o Estado inválidos")
                    return@patch
                }

                val resultado = actualizarEstadoUseCase.execute(id, nuevoEstadoId)
                if (resultado) {
                    call.respond(HttpStatusCode.OK, mapOf("mensaje" to "Estado actualizado correctamente"))
                } else {
                    call.respond(HttpStatusCode.NotFound, "No se pudo actualizar el estado")
                }
            }

        }

    }
}