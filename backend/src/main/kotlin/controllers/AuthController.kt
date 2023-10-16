package controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.UserService
import services.impl.UserServiceImpl
import ua.ukma.edu.danki.models.*
import utils.consts.INCORRECT_CREDENTIALS_MESSAGE


fun Routing.authControllers() {
    val service: UserService = UserServiceImpl()
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
            val principal = call.principal<JWTPrincipal>()
            val email = principal!!.payload.getClaim("email").asString()
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