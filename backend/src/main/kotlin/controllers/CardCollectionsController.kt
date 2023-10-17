package controllers

import exceptions.BadRequestException
import exceptions.ResourceNotFoundException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import services.CardCollectionService
import services.UserService
import services.impl.CardCollectionServiceImpl
import services.impl.UserServiceImpl
import ua.ukma.edu.danki.models.CardCollectionDTO
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.ListOfCollectionsResponse
import utils.consts.USER_NOT_FOUND_MESSAGE
import java.util.*


fun Routing.cardCollectionsControllers() {
    val cardCollectionService: CardCollectionService = CardCollectionServiceImpl()
    val userService: UserService = UserServiceImpl()
    authenticate("auth-jwt") {
        route("/collections/") {
            get {
                val params = call.receiveParameters()
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

                val user = userService.findUser(userId) ?: throw ResourceNotFoundException(USER_NOT_FOUND_MESSAGE)

                val collections = cardCollectionService.getCollections(user, offset, limit, sort, ascending).map {
                    CardCollectionDTO(it.name, it.lastModified, it.id.toString())
                }
                call.respond(ListOfCollectionsResponse(collections))
            }
        }
    }
}