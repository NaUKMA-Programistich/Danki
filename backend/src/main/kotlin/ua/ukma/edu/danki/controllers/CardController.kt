package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.models.GetCardsOfCollection
import ua.ukma.edu.danki.models.ListOfCardsResponse
import ua.ukma.edu.danki.services.CardService
import ua.ukma.edu.danki.services.UserService
import java.util.*

fun Routing.cardController(cardService: CardService, userService: UserService) {
    authenticate("auth-jwt") {
        get<GetCardsOfCollection> {
            try {
                val user = extractUserFromJWT(userService)

                val collection = UUID.fromString(it.collection)
                val offset = it.offset
                val limit = it.limit
                val sortParam = it.sort
                val ascending = it.ascending

                val collections =
                    cardService.readCollectionCards(collection, offset, limit, sortParam, ascending, user.id.value)
                call.respond(ListOfCardsResponse(collections))
            } catch (e: IllegalArgumentException) {
                throw BadRequestException("Bad UUID")
            }
        }
    }
}