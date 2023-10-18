package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.resources.*
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.IllegalAccessException
import ua.ukma.edu.danki.exceptions.ResourceNotFoundException
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.GetUserCollections
import ua.ukma.edu.danki.models.ListOfCollectionsResponse
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.consts.USER_NOT_FOUND_MESSAGE
import ua.ukma.edu.danki.utils.extractEmailFromJWT
import java.util.*

fun Routing.cardCollectionsControllers(cardCollectionService: CardCollectionService, userService: UserService) {
    authenticate("auth-jwt") {
        get<GetUserCollections> {
            val params = call.parameters
            val userId = try {
                UUID.fromString(params["userId"])
            } catch (e: IllegalArgumentException) {
                throw BadRequestException("Invalid UUID")
            }

            val offset = params["offset"]?.toInt() ?: 0
            val limit = params["limit"]?.toInt() ?: 10
            val sortParam = params["sort"] ?: "ByDate"
            val ascending = params["ascending"]?.toBoolean() ?: true

            val sort = when (sortParam) {
                "ByDate" -> CollectionSortParam.ByDate
                else -> CollectionSortParam.ByName
            }

            val email = call.extractEmailFromJWT()

            val user = userService.findUser(userId) ?: throw ResourceNotFoundException(USER_NOT_FOUND_MESSAGE)

            if (user.email != email) throw IllegalAccessException("Only owner can access their own collections")

            val collections = cardCollectionService.getCollections(user, offset, limit, sort, ascending)
            call.respond(ListOfCollectionsResponse(collections))
        }
    }
}