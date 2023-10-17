package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Resource("/collections")
class GetCollectionsCards(val sort: String? = "new")


enum class CollectionSortParam {
    ByName, ByDate
}

@Resource("/collections/")
class GetCollection(val sort: CollectionSortParam)

@Resource("/articles")
class Articles()

@Serializable
data class CardCollectionDTO(
    @SerialName("name")
    val name: String,
    @SerialName("last_modified")
    val lastModified: LocalDateTime,
    @SerialName("id")
    val id: String
)

@Serializable
data class ListOfCollectionsResponse(
    @SerialName("collections")
    val cardCollections: List<CardCollectionDTO>
)