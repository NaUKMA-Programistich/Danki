package ua.ukma.edu.danki.services.impl

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import ua.ukma.edu.danki.exceptions.IllegalAccessException
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
        getExistingUserOrThrow(user)
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

    override suspend fun readCard(card: Long, user: UUID): CardDTO {
        val existingUser = getExistingUserOrThrow(user)
        return DatabaseFactory.dbQuery {
            val cardItself = Card.findById(card) ?: throw ResourceNotFoundException("No such card found")
            throwIfUserCannotAccessCard(cardItself, existingUser)
            cardItself.toCardDTO()
        }
    }

    private fun throwIfUserCannotAccessCard(cardItself: Card, existingUser: User) {
        (UserCardCollections.innerJoin(CardCollections).select(
            where = (CardCollections.id eq cardItself.collection).and(UserCardCollections.user eq existingUser.id)
        ).firstOrNull()
            ?: throw IllegalAccessException("Given user cannot access this card, collection it belongs to is not in this user's records"))
    }

    private fun throwIfUserDoesNotOwnCard(cardItself: Card, existingUser: User) {
        (UserCardCollections.innerJoin(CardCollections).select(
            where = (CardCollections.id eq cardItself.collection).and(UserCardCollections.user eq existingUser.id)
                .and(UserCardCollections.own eq true)
        ).firstOrNull()
            ?: throw IllegalAccessException("Given user does not own the card"))
    }

    private suspend fun getExistingUserOrThrow(user: UUID) =
        userService.findUser(user) ?: throw ResourceNotFoundException("User not found")

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