package ua.ukma.edu.danki

import ua.ukma.edu.danki.controllers.authControllers
import ua.ukma.edu.danki.controllers.cardCollectionsControllers
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.UserRegistrationException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.models.ErrorMsg
import ua.ukma.edu.danki.utils.DatabaseFactory
import ua.ukma.edu.danki.utils.JwtConfig
import ua.ukma.edu.danki.validation.validateUserRequests

private const val PORT = 8080
private const val JWT_SECRET = "secret"
private const val JWT_ISSUER = "https://Danki"
private const val VALIDITY_IN_MS = 36000000L


fun main() {
    embeddedServer(Netty, PORT) {
        module()
    }.start(wait = true)
}

private fun Application.module() {
    DatabaseFactory.init()
    JwtConfig.init(
        System.getProperty("JWT_SECRET") ?: JWT_SECRET,
        System.getProperty("JWT_ISSUER") ?: JWT_ISSUER,
        System.getProperty("VALIDITY_IN_MS")?.toLong() ?: VALIDITY_IN_MS
    )
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)
            validate { JwtConfig.validate(it) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ErrorMsg("Token is not valid or has expired"))
            }
        }
    }
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.reasons.joinToString()))
        }
        exception<UserRegistrationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.message ?: "Unable to register user"))
        }
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.message ?: "Bad request"))
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ErrorMsg("500: $cause"))
        }
    }
    install(RequestValidation) {
        validateUserRequests()
    }
    install(Resources)
    routing {
        cardCollectionsControllers()
        authControllers()
    }
}