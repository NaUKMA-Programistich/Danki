import controllers.cardCollectionsControllers
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import kotlinx.serialization.json.buildJsonObject
import ua.ukma.edu.danki.models.SimpleDto
import utils.DatabaseFactory

private const val PORT = 8080


fun main() {
    embeddedServer(Netty, PORT) {
        module()
    }.start(wait = true)
}

private fun Application.module() {
    DatabaseFactory.init()
    install(ContentNegotiation) {
        json()
    }
    install(Resources)
    routing {
        cardCollectionsControllers()
    }
}