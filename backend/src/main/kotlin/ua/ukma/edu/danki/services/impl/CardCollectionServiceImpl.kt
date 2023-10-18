package ua.ukma.edu.danki.services.impl

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import ua.ukma.edu.danki.exceptions.IllegalAccessException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import java.util.*

class CardCollectionServiceImpl(val userService: UserService) : CardCollectionService {
    override fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<CardCollectionDTO> {
        return runBlocking {
            getCollectionsOfUser(user, sort, ascending, limit, offset)
        }
    }

    private suspend fun getCollectionsOfUser(
        user: User,
        sort: CollectionSortParam,
        ascending: Boolean,
        limit: Int,
        offset: Int
    ) = DatabaseFactory.dbQuery {
        UserCardCollections
            .innerJoin(CardCollections)
            .select(where = UserCardCollections.user eq user.id)
            .orderBy(getSortColumn(sort), if (ascending) SortOrder.ASC else SortOrder.DESC)
            .limit(limit, offset.toLong())
            .map {
                mapResultRowToCardCollection(it).toCardCollectionDTO()
            }
    }

    private fun mapResultRowToCardCollection(it: ResultRow): CardCollection {
        val cardCollection = CardCollection(it[CardCollections.id])
        cardCollection.name = it[CardCollections.name]
        cardCollection.lastModified = it[CardCollections.lastModified]
        return cardCollection
    }

    private fun getSortColumn(sort: CollectionSortParam): Column<*> {
        return when (sort) {
            CollectionSortParam.ByName -> CardCollections.name
            CollectionSortParam.ByDate -> CardCollections.lastModified
        }
    }

    override fun removeCollections(user: User, collections: List<UUID>) {
        TODO("Not yet implemented")
    }

    override fun updateCollection(user: User, collection: CardCollection) {
        TODO("Not yet implemented")
    }

    override fun createCollection(email: String, name: String) {
        runBlocking {
            createNewCollection(
                name,
                userService.findUser(email)
                    ?: throw IllegalAccessException("User trying to create the collection does not exist in the system")
            )
        }
    }

    override fun readCollection(collection: UUID) {
        TODO("Not yet implemented")
    }

    private suspend fun createNewCollection(nameOfNewCollection: String, userOwner: User) {
        DatabaseFactory.dbQuery {
            val createdCollection = CardCollection.new {
                name = nameOfNewCollection
                lastModified = Clock.System.now()
            }
            UserCardCollection.new {
                own = true
                user = userOwner.id
                collection = createdCollection.id
                shared = false
            }
        }
    }
}