import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ua.ukma.edu.danki.models.SimpleDto

private const val PORT = 8080

fun main() {
    embeddedServer(Netty, PORT) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/") {
                call.respond(SimpleDto("Hello, world!"))
            }
        }
    }.start(wait = true)
}