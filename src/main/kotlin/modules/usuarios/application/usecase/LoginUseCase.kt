import com.alilopez.common.infrastructure.security.JwtConfig
import com.alilopez.modules.usuarios.application.usecase.RegistrarUseCase
import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class LoginUseCase(
    private val repository: UsuarioRepository,
    private val registrarUseCase: RegistrarUseCase
) {
    suspend fun execute(email: String, googleId: String, nombre: String): String {
        var usuario = repository.verPorEmail(email)

        if (usuario == null) {
            val rolAsignado = when {
                email.endsWith("@tuapp.com")-> 3
                email.endsWith("@sarc.gob.mx") -> 1
                else -> 2
            }

            val nuevoUsuario = Usuario(
                nombre = nombre,
                email = email,
                googleId = googleId,
                idRol = rolAsignado,
                primerApellido = "",
                segundoApellido = "",
                edad = 0
            )
            usuario = registrarUseCase.execute(nuevoUsuario)
        }

        return JwtConfig.generateToken(usuario!!)
    }
}