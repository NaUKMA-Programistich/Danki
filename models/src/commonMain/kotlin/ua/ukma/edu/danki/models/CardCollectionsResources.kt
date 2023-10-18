package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Resource("/collections")
class GetCollectionsCards(val sort: String? = "new")


enum class CollectionSortParam {
    ByName, ByDate
}

// Returns collections of the authenticated user
@Resource("/collections")
data class GetUserCollections(
    val sort: CollectionSortParam = CollectionSortParam.ByDate,
    val offset: Int = 0,
    val limit: Int = 0,
    val ascending: Boolean = true
)


@Resource("/articles")
class Articles()

@Serializable
data class UserCardCollectionDTO(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("last_modified")
    val lastModified: Instant,
    val own: Boolean,
    val favorite: Boolean
)

@Serializable
data class CreateCardCollectionRequest(
    @SerialName("name")
    val name: String
)

@Serializable
data class CreateCardCollectionResponse(
    @SerialName("uuid")
    val uuid: String
)

@Serializable
data class ListOfCollectionsResponse(
    @SerialName("collections")
    val cardCollections: List<UserCardCollectionDTO>
)