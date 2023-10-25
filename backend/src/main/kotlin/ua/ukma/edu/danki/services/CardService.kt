package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.CardSortParam
import java.util.UUID

interface CardService {
    suspend fun createCard(card: CardDTO, user: UUID): Long
    suspend fun readCard(card: Long, user: UUID)
    suspend fun deleteCards(card: List<CardDTO>, user: UUID)
    suspend fun updateCard(card: CardDTO, user: UUID)
    suspend fun readCollectionCards(
        collection: UUID,
        offset: Int,
        limit: Int,
        sort: CardSortParam,
        ascending: Boolean,
        user: UUID
    )
}