package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
data class CardDTO(val id: Long? = null, val term: String, val definition: String)

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