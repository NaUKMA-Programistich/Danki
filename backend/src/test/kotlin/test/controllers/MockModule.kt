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
import ua.ukma.edu.danki.DICTIONARY_PATH
import ua.ukma.edu.danki.controllers.*
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.UserRegistrationException
import ua.ukma.edu.danki.models.ErrorMsg
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.CardService
import ua.ukma.edu.danki.services.RecentsService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.services.impl.*
import ua.ukma.edu.danki.utils.DatabaseFactory
import ua.ukma.edu.danki.utils.auth.JwtConfig
import ua.ukma.edu.danki.validation.validateUserRequests

fun Application.mockModule(
    cardCollectionService: CardCollectionService,
    userService: UserService,
    cardService: CardService,
    recentsService: RecentsService
) {
    DatabaseFactory.init(
        environment.config.property("db.driver").getString(),
        environment.config.property("db.url").getString()
    )
    JwtConfig.init(
        environment.config.property("jwt.secret").getString(),
        environment.config.property("jwt.issuer").getString(),
        environment.config.property("jwt.validity").getString().toLong(),
        userService
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
        exception<IllegalAccessException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ErrorMsg(cause.message ?: "Unable to register user"))
        }
        exception<UserRegistrationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.message ?: "Unable to register user"))
        }
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.message ?: "Bad request"))
        }
        exception<io.ktor.server.plugins.BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorMsg(cause.message ?: "Bad request"))
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ErrorMsg("500: $cause"))
            cause.printStackTrace()
        }
    }
    install(RequestValidation) {
        validateUserRequests()
    }
    install(Resources)
    routing {
        cardCollectionsControllers(cardCollectionService, userService)
        authControllers(userService)
        dictionaryController(
            { DictionaryServiceImpl(DICTIONARY_PATH, recentsService) },
            userService
        )
        cardController(cardService, userService)
        recentsController(recentsService, userService)
    }
}

fun Application.configureProductionModuleForTests() {
    val userService = UserServiceImpl()
    val cardCollectionService = CardCollectionServiceImpl(userService)
    val cardService = CardServiceImpl(userService, cardCollectionService)
    val recentsService = RecentsServiceImpl()
    this.mockModule(
        userService = userService,
        cardCollectionService = cardCollectionService,
        cardService = cardService,
        recentsService = recentsService
    )
}