package ua.ukma.edu.danki.services.impl

import ua.ukma.edu.danki.exceptions.ResourceNotFoundException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.CardService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import java.util.*

class CardServiceImpl(
    private val userService: UserService,
    private val cardCollectionService: CardCollectionService
) : CardService {
    override suspend fun createCard(card: CardDTO, user: UUID): Long {
        userService.findUser(user) ?: throw ResourceNotFoundException("User not found")
        val recents = cardCollectionService.getDefaultCollectionOfUser(user)
            ?: throw Exception("Internal server error, Recents collection for the specified user does not exist")
        val createdCard = DatabaseFactory.dbQuery {
            val collectionItself = CardCollection.findById(recents.collection)
                ?: throw Exception("Internal server error, Recents collection for the specified user does not exist")
            Card.new {
                term = card.term
                definition = card.definition
                collection = collectionItself.id
            }
        }
        return createdCard.id.value
    }

    override suspend fun readCard(card: Long, user: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCards(card: List<CardDTO>, user: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCard(card: CardDTO, user: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun readCollectionCards(
        collection: UUID,
        offset: Int,
        limit: Int,
        sort: CardSortParam,
        ascending: Boolean,
        user: UUID
    ) {
        TODO("Not yet implemented")
    }
}