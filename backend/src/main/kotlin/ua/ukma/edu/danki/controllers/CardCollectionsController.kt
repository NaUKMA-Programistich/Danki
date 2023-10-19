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
import ua.ukma.edu.danki.utils.toUUID
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
            val favorite = params["favorite"]?.toBoolean() ?: false

            val sort = when (sortParam) {
                "ByDate" -> CollectionSortParam.ByDate
                else -> CollectionSortParam.ByName
            }

            val collections = cardCollectionService.getCollections(user, offset, limit, sort, ascending, favorite)
            call.respond(ListOfCollectionsResponse(collections))
        }

        post<CreateCardCollectionRequest>("/collections/") {
            val email = call.extractEmailFromJWT()
            val uuidOfCreatedRelation = cardCollectionService.createCollection(email, it.name)
            call.respond(CreateCardCollectionResponse(uuidOfCreatedRelation.toString()))
        }

        put<UpdateCollectionRequest>("/collections/") {
            val user = extractUserFromJWT(userService)
            val internalCardCollectionDTO: InternalCardCollectionDTO =
                cardCollectionService.readCollection(user, UUID.fromString(it.uuid)) ?: throw ResourceNotFoundException(
                    "Collection requested for update was not found"
                )
            internalCardCollectionDTO.name = it.name ?: internalCardCollectionDTO.name
            internalCardCollectionDTO.favorite = it.favorite
            cardCollectionService.updateCollection(internalCardCollectionDTO)
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

        post<ShareCollectionRequest>("/collections/share") {
            val user = extractUserFromJWT(userService)
            val id = cardCollectionService.shareCollection(user, it.uuid.toUUID())
            call.respond(ShareCollectionResponse(id))
        }

        post<ReadSharedCollectionRequest>("/collections/share") {
            val user = extractUserFromJWT(userService)
            call.respond(cardCollectionService.getSharedCollection(user, it.id))
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.extractUserFromJWT(
    userService: UserService
) = userService.findUser(call.extractEmailFromJWT()) ?: throw ResourceNotFoundException(
    USER_NOT_FOUND_MESSAGE
)