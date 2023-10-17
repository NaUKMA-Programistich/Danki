package services.impl

import kotlinx.coroutines.runBlocking
import models.CardCollection
import models.CardCollections
import models.User
import models.UserCardCollections
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import services.CardCollectionService
import ua.ukma.edu.danki.models.CollectionSortParam
import utils.DatabaseFactory
import java.util.*

class CardCollectionServiceImpl : CardCollectionService {
    override fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<CardCollection> {
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
                mapResultRowToCardCollection(it)
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

    override fun createCollection(user: User) {
        TODO("Not yet implemented")
    }

    override fun readCollection(collection: UUID) {
        TODO("Not yet implemented")
    }
}