package test.controllers

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.controllers.authControllers
import ua.ukma.edu.danki.controllers.cardCollectionsControllers
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.UserRegistrationException
import ua.ukma.edu.danki.models.ErrorMsg
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import ua.ukma.edu.danki.utils.JwtConfig
import ua.ukma.edu.danki.validation.validateUserRequests

fun Application.mockModule(cardCollectionServiceMock: CardCollectionService, userServiceMock: UserService) {
    DatabaseFactory.init(
        environment.config.property("db.driver").getString(),
        environment.config.property("db.url").getString()
    )
    JwtConfig.init(
        environment.config.property("jwt.secret").getString(),
        environment.config.property("jwt.issuer").getString(),
        environment.config.property("jwt.validity").getString().toLong(),
        userServiceMock
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
        cardCollectionsControllers(cardCollectionServiceMock, userServiceMock)
        authControllers(userServiceMock)
    }
}