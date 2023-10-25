package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.models.*
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

        post<CardDTO>("/cards/new") {
            val user = extractUserFromJWT(userService)
            call.respond(CardCreatedResponse(cardService.createCard(it, user.id.value)))
        }

        post<CreateCardInCollectionRequest>("/cards/new") {
            val user = extractUserFromJWT(userService)
            call.respond(
                CardCreatedResponse(
                    cardService.createCardInCollection(
                        it.card,
                        UUID.fromString(it.collection),
                        user.id.value
                    )
                )
            )
        }

        get<GetCard> {
            val user = extractUserFromJWT(userService)
            call.respond(cardService.readCard(it.card, user.id.value))
        }

        post<DeleteCardsRequest>("/cards/delete") {
            val user = extractUserFromJWT(userService)
            cardService.deleteCards(it.cardDTOS, user.id.value)
            call.respond(GenericBooleanResponse(true))
        }

        put<CardDTO>("/cards/update") {
            val user = extractUserFromJWT(userService)
            cardService.updateCard(it, user.id.value)
            call.respond(GenericBooleanResponse(true))
        }

        put<MoveCardToCollectionRequest>("/cards/move") {
            val user = extractUserFromJWT(userService)
            cardService.moveCardToCollection(it.card, user.id.value, UUID.fromString(it.collection))
            call.respond(GenericBooleanResponse(true))
        }
    }
}