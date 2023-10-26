package ua.ukma.edu.danki.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.consts.INCORRECT_CREDENTIALS_MESSAGE
import ua.ukma.edu.danki.utils.extractEmailFromJWT


fun Routing.authControllers(service: UserService) {
    post<UserAuthRequest>("login") {
        val response = UserAuthResponse(service.authenticateUser(it))
        if (response.jwt == null)
            respondWithBadRequestError(call, ErrorMsg(INCORRECT_CREDENTIALS_MESSAGE))
        else
            call.respond(response)
    }

    post<UserRegisterRequest>("register") {
        service.registerUser(it)
        call.respond(UserRegisterResponse(true))
    }

    authenticate("auth-jwt") {
        get("/echo-email") {
            val email = call.extractEmailFromJWT()
            call.respondText(email)
        }
    }

    get("/hello-world") {
        call.respondText("Hello indeed!")
    }
}

private suspend fun respondWithBadRequestError(call: ApplicationCall, msg: Any) {
    call.respond(HttpStatusCode.BadRequest, msg)
}