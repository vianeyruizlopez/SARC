package com.alilopez.modules.reportes.infrastructure.rest

import com.alilopez.modules.reportes.application.usecase.*
import com.alilopez.modules.reportes.domain.model.Reporte
import com.alilopez.modules.reportes.infrastructure.rest.dto.toResponse
import com.alilopez.modules.reportes.infrastructure.services.CloudinaryService
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
    val verEstadisticasUsuarioUseCase by inject<VerEstadisticasUsuarioUseCase>()
    val verEstadisticasGlobalesUseCase by inject<VerEstadisticasGlobalesUseCase>()
    val verReportesMapaUseCase by inject<VerReportesMapaUseCase>()
    val verListaReportesAdminUseCase by inject<VerListaReportesAdminUseCase>()
    val verEstadisticasAdmiUseCase by inject<VerEstadisticasAdmiUseCase>()

    val cloudinaryService = CloudinaryService()

    authenticate("auth-jwt") {
        route("/reporte") {

            get {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val estadoFiltro = call.request.queryParameters["estado"]?.toIntOrNull()
                val textoBusqueda = call.request.queryParameters["q"]

                try {
                    val reportes = verReporteUseCase.execute(rol, estadoFiltro, textoBusqueda)
                    call.respond(HttpStatusCode.OK, reportes.map { it.toResponse() })
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
                }
            }

            get("/mapa") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val idEstado = call.request.queryParameters["estado"]?.toIntOrNull()
                val idIncidencia = call.request.queryParameters["incidencia"]?.toIntOrNull()

                try {
                    val puntos = verReportesMapaUseCase.execute(rol, idEstado, idIncidencia)
                    call.respond(HttpStatusCode.OK, puntos)
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al cargar puntos del mapa")
                }
            }

            get("/admin/reportes") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val idEstado = call.request.queryParameters["estado"]?.toIntOrNull()
                val idIncidencia = call.request.queryParameters["incidencia"]?.toIntOrNull()

                try {
                    val lista = verListaReportesAdminUseCase.execute(rol, idEstado, idIncidencia)
                    call.respond(HttpStatusCode.OK, lista)
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
                }
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (rol == 2) {
                    val multipart = call.receiveMultipart()
                    var titulo = ""; var descripcion = ""; var idUsuario = 0
                    var idIncidencia = 0; var latitud = 0.0; var longitud = 0.0
                    var idEstado = 1; var imageUrl: String? = null
                    var ubicacion = ""

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
                                    "ubicacion" -> ubicacion = part.value
                                }
                            }
                            is PartData.FileItem -> {
                                imageUrl = cloudinaryService.uploadImage(part.streamProvider())
                            }
                            else -> part.dispose()
                        }
                    }

                    val reporteDomain = Reporte(
                        titulo = titulo, descripcion = descripcion,
                        id_usuario = idUsuario, id_incidencia = idIncidencia,
                        latitud = latitud, longitud = longitud,
                        id_estado = idEstado, imagen = imageUrl, ubicacion = ubicacion
                    )

                    val nuevo = crearReporteUseCase.execute(reporteDomain)
                    call.respond(HttpStatusCode.Created, nuevo.toResponse())
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Solo ciudadanos pueden crear reportes")
                }
            }

            get("/usuario/{idUsuario}") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val idUsuarioToken = principal?.payload?.getClaim("id")?.asInt() ?: 0
                val idUsuarioPath = call.parameters["idUsuario"]?.toIntOrNull()

                val estadoFiltro = call.request.queryParameters["estado"]?.toIntOrNull()
                val textoBusqueda = call.request.queryParameters["q"]

                if (idUsuarioPath == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido")
                    return@get
                }
                if (rol == 2 && idUsuarioPath != idUsuarioToken) {
                    call.respond(HttpStatusCode.Forbidden, "No puedes ver reportes de otros usuarios")
                    return@get
                }

                try {
                    val misReportes = verPorUsuarioUseCase.execute(idUsuarioPath, rol, estadoFiltro, textoBusqueda)
                    call.respond(HttpStatusCode.OK, misReportes.map { it.toResponse() })
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
                }
            }

            get("/usuario/{idUsuario}/estadisticas") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                val idUsuarioToken = principal?.payload?.getClaim("id")?.asInt() ?: 0
                val idUsuarioPath = call.parameters["idUsuario"]?.toIntOrNull()

                if (idUsuarioPath == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido")
                    return@get
                }

                if (rol == 2 && idUsuarioPath != idUsuarioToken) {
                    call.respond(HttpStatusCode.Forbidden, "No puedes ver estadísticas de otros usuarios")
                    return@get
                }

                try {
                    val estadisticas = verEstadisticasUsuarioUseCase.execute(idUsuarioPath, rol)
                    call.respond(HttpStatusCode.OK, estadisticas)
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Error de acceso")
                }
            }


            get("/estadisticas/globalAdmi") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                try {
                    val estadisticas = verEstadisticasAdmiUseCase.execute(rol)
                    call.respond(HttpStatusCode.OK, estadisticas)
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "No tienes permisos")
                } catch (e: Exception) {
                    println("Error en Estadísticas Globales: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Error al generar estadísticas")
                }
            }

            get("/estadisticas/global") {
                val principal = call.principal<JWTPrincipal>()
                val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                try {
                    val estadisticas = verEstadisticasGlobalesUseCase.execute(rol)
                    call.respond(HttpStatusCode.OK, estadisticas)
                } catch (e: IllegalAccessException) {
                    call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
                }
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
                    val principal = call.principal<JWTPrincipal>()
                    val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                    if (rol == 2) {
                        val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest, "ID Inválido")
                        val multipart = call.receiveMultipart()

                        var titulo = ""; var descripcion = ""; var idIncidencia = 0
                        var latitud = 0.0; var longitud = 0.0; var idEstado = 0
                        var newImageUrl: String? = null
                        var ubicacion = ""

                        multipart.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    when (part.name) {
                                        "titulo" -> titulo = part.value
                                        "descripcion" -> descripcion = part.value
                                        "ubicacion" -> ubicacion = part.value
                                        "id_incidencia" -> idIncidencia = part.value.toIntOrNull() ?: 0
                                        "latitud" -> latitud = part.value.toDoubleOrNull() ?: 0.0
                                        "longitud" -> longitud = part.value.toDoubleOrNull() ?: 0.0
                                        "id_estado" -> idEstado = part.value.toIntOrNull() ?: 0
                                    }
                                }
                                is PartData.FileItem -> {
                                    if (part.originalFileName?.isNotEmpty() == true) {
                                        newImageUrl = cloudinaryService.uploadImage(part.streamProvider())
                                    }
                                }
                                else -> part.dispose()
                            }
                        }


                        val reporteData = Reporte(
                            id_reporte = id,
                            titulo = titulo,
                            descripcion = descripcion,
                            id_usuario = 0,
                            id_incidencia = idIncidencia,
                            latitud = latitud,
                            longitud = longitud,
                            id_estado = idEstado,
                            imagen = newImageUrl,
                            ubicacion = ubicacion
                        )

                        val resultado = actualizarReporteUseCase.execute(id, reporteData)

                        if (resultado != null) {
                            call.respond(HttpStatusCode.OK, resultado.toResponse())
                        } else {
                            call.respond(HttpStatusCode.NotFound, "No se encontró el reporte para actualizar")
                        }
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Solo ciudadanos pueden editar reportes")
                    }
                }
                delete {
                    val principal = call.principal<JWTPrincipal>()
                    val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                    if (rol == 3) {
                        val id = call.parameters["id"]?.toIntOrNull()
                        if (id != null) {
                            eliminarReporteUseCase.execute(id)
                            call.respond(HttpStatusCode.OK, "Reporte eliminado")
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                        }
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Solo el SuperAdmin puede eliminar reportes")
                    }
                }

                patch("/estado") {
                    val principal = call.principal<JWTPrincipal>()
                    val rol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                    if (rol == 1) {
                        val id = call.parameters["id"]?.toIntOrNull()
                        val nuevoEstadoId = call.receive<Map<String, Int>>()["id_estado"]

                        if (id == null || nuevoEstadoId == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID o Estado inválidos")
                            return@patch
                        }

                        val resultado = actualizarEstadoUseCase.execute(id, nuevoEstadoId)
                        if (resultado) {
                            call.respond(HttpStatusCode.OK, mapOf("mensaje" to "Estado actualizado"))
                        } else {
                            call.respond(HttpStatusCode.NotFound, "No se pudo actualizar")
                        }
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Solo administradores de staff pueden cambiar el estado")
                    }
                }
            }
        }
    }
}