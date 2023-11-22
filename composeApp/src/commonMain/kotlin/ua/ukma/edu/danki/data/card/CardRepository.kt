package ua.ukma.edu.danki.data.card

import ua.ukma.edu.danki.models.*

interface CardRepository {
    suspend fun getCardsOfCollection(request: GetCardsOfCollection): ListOfCardsResponse?
    suspend fun createNewCard(request: CardDTO): CardCreatedResponse?
    suspend fun createCardInCollection(request: CreateCardInCollectionRequest): CardCreatedResponse?
    suspend fun getCard(request: GetCard): CardDTO?
    suspend fun deleteCards(request: DeleteCardsRequest): GenericBooleanResponse?
    suspend fun updateCard(request: CardDTO): GenericBooleanResponse?
    suspend fun moveCardToCollection(request: MoveCardToCollectionRequest): GenericBooleanResponse?
}