package controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.models.UserRegisterRequest
import io.ktor.server.routing.*
import services.UserService
import services.impl.UserServiceImpl
import ua.ukma.edu.danki.models.UserAuthResponse
import ua.ukma.edu.danki.models.UserRegisterResponse


fun Routing.authControllers() {
    val service: UserService = UserServiceImpl()
    post<UserAuthRequest>("login") {
        val response = UserAuthResponse(service.authenticateUser(it))
        if (response.jwt == null)
            call.respond(HttpStatusCode.BadRequest, response)
        else
            call.respond(response)
    }

    post<UserRegisterRequest>("register") {
        val response = UserRegisterResponse(service.registerUser(it))
        if (!response.success)
            call.respond(HttpStatusCode.BadRequest, response)
        else
            call.respond(response)
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