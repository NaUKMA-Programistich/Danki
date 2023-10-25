package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CardDTO
    (
    val id: Long? = null,
    val term: String,
    val definition: String,
    val timeAdded: Instant? = null,
    val lastModified: Instant? = null
)

@Serializable
data class ListOfCardsResponse(val cards: List<CardDTO>)

@Serializable
data class CardCreatedResponse(val cardId: Long)

@Serializable
enum class CardSortParam {
    ByTerm, ByTimeAdded, ByLastModified
}

// Returns cards of the specified collection if such can be found among collections of the user
@Resource("/cards")
data class GetCardsOfCollection(
    val collection: String,
    val offset: Int = 0,
    val limit: Int = 10,
    val sort: CardSortParam = CardSortParam.ByTimeAdded,
    val ascending: Boolean = true
)

@Serializable
data class CreateCardInCollectionRequest(val card: CardDTO, val collection: String)

// Returns CardDTO of the card by the given id if the logged-in user can access it
@Resource("/card")
data class GetCard(
    val card: Long
)

@Serializable
data class DeleteCardsRequest(val cardIds: List<Long>)

@Serializable
data class MoveCardToCollectionRequest(val card: Long, val collection: String)