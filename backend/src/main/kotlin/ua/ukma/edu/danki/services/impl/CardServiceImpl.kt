package ua.ukma.edu.danki.services.impl

import kotlinx.datetime.Clock
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

    override suspend fun deleteCards(cardDTOS: List<CardDTO>, user: UUID) {
        val existingUser = getExistingUserOrThrow(user)
        DatabaseFactory.dbQuery {
            cardDTOS.forEach {
                val cardItself =
                    findCardByDtoOrThrow(it, "One the cards was not found")
                throwIfUserDoesNotOwnCard(cardItself, existingUser)
                cardItself.delete()
            }
        }
    }

    override suspend fun updateCard(card: CardDTO, user: UUID) {
        DatabaseFactory.dbQuery {
            val existingUser = getExistingUserOrThrow(user)
            val cardItself = findCardByDtoOrThrow(card, "No card was found by provided id")
            throwIfUserDoesNotOwnCard(cardItself, existingUser)
            cardItself.term = card.term
            cardItself.lastModified = Clock.System.now()
            cardItself.definition = card.definition
            cardItself.flush()
        }
    }

    override suspend fun moveCardToCollection(card: Long, user: UUID, collection: UUID) {
        DatabaseFactory.dbQuery {
            val existingUser = getExistingUserOrThrow(user)
            val cardItself = findCardByIdOrThrow(card, "No card was found by provided id")
            throwIfUserDoesNotOwnCard(cardItself, existingUser)
            val collectionDTO = cardCollectionService.readCollection(existingUser, collection)
                ?: throw ResourceNotFoundException("No such collection found for user")
            val collectionItself = CardCollection.findById(collectionDTO.collection)
                ?: throw ResourceNotFoundException("No such collection found for user")
            cardItself.collection = collectionItself.id
            cardItself.flush()
        }
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

    private fun findCardByDtoOrThrow(it: CardDTO, msg: String) =
        (Card.findById(it.id ?: throw ResourceNotFoundException(msg))
            ?: throw ResourceNotFoundException(msg))

    private fun findCardByIdOrThrow(it: Long, msg: String) =
        (Card.findById(it)
            ?: throw ResourceNotFoundException(msg))

    private suspend fun getExistingUserOrThrow(user: UUID) =
        userService.findUser(user) ?: throw ResourceNotFoundException("User not found")
}