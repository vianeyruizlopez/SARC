package modules.products.infrastructure.rest

import com.alilopez.modules.products.application.DeleteProductUseCase
import com.alilopez.modules.products.application.UpdateProductUseCase
import com.alilopez.modules.products.infrastructure.rest.dto.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import modules.products.application.CreateProductUseCase
import modules.products.application.GetProductsUseCase
import modules.products.infrastructure.rest.dto.CreateProductRequest
import org.koin.core.logger.Logger
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import java.util.UUID
import kotlin.math.log

fun Route.productRoutes() {
    val createProductUseCase by inject<CreateProductUseCase>()
    val getProductsUseCase by inject<GetProductsUseCase>()
    val updateProductUseCase by inject<UpdateProductUseCase>()
    val deleteProductUseCase by inject<DeleteProductUseCase>()


    route("/products") {

        route("/{id}") {
            put {
                val id = UUID.fromString(call.parameters["id"])
                val request = call.receive<CreateProductRequest>() // Reutilizamos el DTO o creamos un UpdateProductRequest

                val updatedProduct = updateProductUseCase.execute(id, request.toDomain())
                call.respond(HttpStatusCode.OK, updatedProduct.toResponse())
            }

            delete {
                val id = UUID.fromString(call.parameters["id"])
                deleteProductUseCase.execute(id)
                call.respond(HttpStatusCode.NoContent) // 204 No Content es el estándar para DELETE exitoso
            }
        }

        get {
            val products = getProductsUseCase.execute()
            // Mapeamos la lista de objetos de dominio a DTOs serializables
            val response = products.map { it.toResponse() }
            call.respond(response)
        }

        post {
            print("Hola mundo")
            val request = call.receive<CreateProductRequest>()
            val result = createProductUseCase.execute(request.toDomain())
            call.respond(HttpStatusCode.Created, result.toResponse())
        }
    }
}