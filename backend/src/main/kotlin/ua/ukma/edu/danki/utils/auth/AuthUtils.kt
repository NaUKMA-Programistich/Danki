package ua.ukma.edu.danki.utils.auth

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import ua.ukma.edu.danki.exceptions.ResourceNotFoundException
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.consts.USER_NOT_FOUND_MESSAGE
import ua.ukma.edu.danki.utils.extractEmailFromJWT
import ua.ukma.edu.danki.utils.extractEmailFromOptionalJWT

suspend fun PipelineContext<Unit, ApplicationCall>.extractOptionalUserFromJWT(
    userService: UserService
) = call.extractEmailFromOptionalJWT()?.let { userService.findUser(it) }

suspend fun PipelineContext<Unit, ApplicationCall>.extractUserFromJWT(
    userService: UserService
) = userService.findUser(call.extractEmailFromJWT()) ?: throw ResourceNotFoundException(
    USER_NOT_FOUND_MESSAGE
)
