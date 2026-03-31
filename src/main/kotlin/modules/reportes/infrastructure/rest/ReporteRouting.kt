package com.alilopez.modules.reportes.infrastructure.rest

import com.alilopez.modules.reportes.application.usecase.*
import com.alilopez.modules.reportes.infrastructure.rest.dto.CreateReporteRequest
import com.alilopez.modules.reportes.infrastructure.rest.dto.toResponse // Importante: tu mapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reporteRouting() {
    val crearReporteUseCase by inject<CrearReporteUseCase>()
    val listaReporteUseCase by inject<VerIdReporteUseCase>()
    val verIdReporteUseCase by inject<VerIdReporteUseCase>()
    val actualizarReporteUseCase by inject<ActualizarReporteUseCase>()
    val eliminarReporteUseCase by inject<EliminarReporteUseCase>()
    route("/reporte") {
        get {
            val reportes = listaReporteUseCase.execute()
            call.respond(HttpStatusCode.OK, reportes.map { it.toResponse() })
        }
        post {
            val request = call.receive<CreateReporteRequest>()
            val nuevo = crearReporteUseCase.execute(request.toDomain())
            call.respond(HttpStatusCode.Created, nuevo.toResponse())
        }
        route("/{id}") {
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
        }


    }
}