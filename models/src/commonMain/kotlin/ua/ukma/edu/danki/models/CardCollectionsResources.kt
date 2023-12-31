package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class CollectionSortParam {
    ByName, ByDate
}

// Returns collections of the authenticated user
// favorite property set to false will return all collections, true - only the ones listed as favorite
@Resource("/collections")
data class GetUserCollections(
    val sort: CollectionSortParam = CollectionSortParam.ByDate,
    val favorite: Boolean = false,
    val offset: Int = 0,
    val limit: Int = 10,
    val ascending: Boolean = true
)

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
data class DeleteCollectionsRequest(
    @SerialName("collections")
    val collections: List<String>
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
data class UpdateCollectionRequest(
    val uuid: String,
    val favorite: Boolean,
    val name: String? = null
)

@Serializable
data class ShareCollectionRequest(
    val uuid: String
)

@Serializable
data class ShareCollectionResponse(
    val id: Long
)

@Serializable
data class ReadSharedCollectionRequest(
    val id: Long
)

@Serializable
data class ListOfCollectionsResponse(
    @SerialName("collections")
    val cardCollections: List<UserCardCollectionDTO>
)