package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import ua.ukma.edu.danki.models.GetRecentTerms
import ua.ukma.edu.danki.models.RecentTerm
import ua.ukma.edu.danki.models.RecentTerms
import ua.ukma.edu.danki.services.RecentsService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.auth.extractUserFromJWT


fun Routing.recentsController(recentsService: RecentsService, userService: UserService) {

    authenticate("auth-jwt") {

        get<GetRecentTerms> {
            val user = extractUserFromJWT(userService)
            val history = recentsService.getHistory(user, it.offset, it.limit)
            call.respond(RecentTerms(history.map { term -> RecentTerm(term.term) }))
        }
    }
}
