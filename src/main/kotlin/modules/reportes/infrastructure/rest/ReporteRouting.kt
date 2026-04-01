package com.alilopez.modules.reportes.infrastructure.rest

import com.alilopez.modules.reportes.application.usecase.*
import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.infrastructure.rest.dto.CreateReporteRequest
import com.alilopez.modules.reportes.infrastructure.rest.dto.toResponse
import com.alilopez.modules.reportes.infrastructure.services.CloudinaryService
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
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

    val cloudinaryService = CloudinaryService()

    route("/reporte") {
        get {
            val reportes = verReporteUseCase.execute()
            call.respond(HttpStatusCode.OK, reportes.map { it.toResponse() })
        }
        post {
            val multipart = call.receiveMultipart()

            var titulo = ""; var descripcion = ""; var idUsuario = 0
            var idIncidencia = 0; var latitud = 0.0; var longitud = 0.0
            var idEstado = 1; var imageUrl: String? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "titulo" -> titulo = part.value
                            "descripcion" -> descripcion = part.value
                            "id_usuario" -> idUsuario = part.value.toInt()
                            "id_incidencia" -> idIncidencia = part.value.toInt()
                            "latitud" -> latitud = part.value.toDouble()
                            "longitud" -> longitud = part.value.toDouble()
                            "id_estado" -> idEstado = part.value.toInt()
                        }
                    }
                    is PartData.FileItem -> {
                        imageUrl = cloudinaryService.uploadImage(part.streamProvider())
                    }
                    else -> part.dispose()
                }
            }

            val reporteDomain = Reporte(
                titulo = titulo,
                descripcion = descripcion,
                id_usuario = idUsuario,
                id_incidencia = idIncidencia,
                latitud = latitud,
                longitud = longitud,
                id_estado = idEstado,
                imagen = imageUrl
            )

            val nuevo = crearReporteUseCase.execute(reporteDomain)
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

                val multipart = call.receiveMultipart()
                val cloudinaryService = CloudinaryService()

                // Variables para capturar los datos del formulario
                var titulo = ""; var descripcion = ""; var idIncidencia = 0
                var latitud = 0.0; var longitud = 0.0; var idEstado = 0
                var idUsuario = 0
                var newImageUrl: String? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "titulo" -> titulo = part.value
                                "descripcion" -> descripcion = part.value
                                "id_incidencia" -> idIncidencia = part.value.toInt()
                                "id_usuario" -> idUsuario = part.value.toInt()
                                "latitud" -> latitud = part.value.toDouble()
                                "longitud" -> longitud = part.value.toDouble()
                                "id_estado" -> idEstado = part.value.toInt()
                            }
                        }
                        is PartData.FileItem -> {
                            // Solo subimos a Cloudinary si el usuario mandó un archivo nuevo
                            if (part.originalFileName?.isNotEmpty() == true) {
                                newImageUrl = cloudinaryService.uploadImage(part.streamProvider())
                            }
                        }
                        else -> part.dispose()
                    }
                }

                // Buscamos el reporte actual para no perder datos que no se enviaron
                val reporteActual = verIdReporteUseCase.execute(id)
                if (reporteActual == null) {
                    call.respond(HttpStatusCode.NotFound, "No se encontró el reporte")
                    return@put
                }

                // Creamos el objeto con los datos nuevos o mantenemos los viejos si vienen vacíos
                val reporteEditado = Reporte(
                    id_reporte = id,
                    titulo = if (titulo.isNotEmpty()) titulo else reporteActual.titulo,
                    descripcion = if (descripcion.isNotEmpty()) descripcion else reporteActual.descripcion,
                    id_usuario = if (idUsuario != 0) idUsuario else reporteActual.id_usuario,
                    id_incidencia = if (idIncidencia != 0) idIncidencia else reporteActual.id_incidencia,
                    latitud = if (latitud != 0.0) latitud else reporteActual.latitud,
                    longitud = if (longitud != 0.0) longitud else reporteActual.longitud,
                    id_estado = if (idEstado != 0) idEstado else reporteActual.id_estado,
                    imagen = newImageUrl ?: reporteActual.imagen // Si no hay imagen nueva, dejamos la que estaba
                )

                val resultado = actualizarReporteUseCase.execute(id, reporteEditado)

                if (resultado != null) {
                    call.respond(HttpStatusCode.OK, resultado.toResponse())
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al actualizar")
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