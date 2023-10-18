package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.ResourceNotFoundException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.consts.USER_NOT_FOUND_MESSAGE
import ua.ukma.edu.danki.utils.extractEmailFromJWT
import java.lang.IllegalArgumentException
import java.util.*

fun Routing.cardCollectionsControllers(cardCollectionService: CardCollectionService, userService: UserService) {
    authenticate("auth-jwt") {
        get<GetUserCollections> {
            val params = call.parameters

            val user = extractUserFromJWT(userService)

            val offset = params["offset"]?.toInt() ?: 0
            val limit = params["limit"]?.toInt() ?: 10
            val sortParam = params["sort"] ?: "ByDate"
            val ascending = params["ascending"]?.toBoolean() ?: true

            val sort = when (sortParam) {
                "ByDate" -> CollectionSortParam.ByDate
                else -> CollectionSortParam.ByName
            }

            val collections = cardCollectionService.getCollections(user, offset, limit, sort, ascending)
            call.respond(ListOfCollectionsResponse(collections))
        }

        post<CreateCardCollectionRequest>("/collections/") {
            val email = call.extractEmailFromJWT()
            val uuidOfCreatedRelation = cardCollectionService.createCollection(email, it.name)
            call.respond(CreateCardCollectionResponse(uuidOfCreatedRelation.toString()))
        }

        post<UpdateCollectionRequest>("/collections/") {
            val email = call.extractEmailFromJWT()
            //val uuidOfCreatedRelation = cardCollectionService.updateCollection(email, it.name)
            call.respond(GenericBooleanResponse(true))
        }

        post<DeleteCollectionsRequest>("/collections/delete") {
            val user = extractUserFromJWT(userService)
            try {
                cardCollectionService.removeCollections(user, it.collections.map { uuid -> UUID.fromString(uuid) })
                call.respond(GenericBooleanResponse(true))
            } catch (e: IllegalArgumentException) {
                throw BadRequestException("Malformed ids of collections provided")
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.extractUserFromJWT(
    userService: UserService
) = userService.findUser(call.extractEmailFromJWT()) ?: throw ResourceNotFoundException(
    USER_NOT_FOUND_MESSAGE
)