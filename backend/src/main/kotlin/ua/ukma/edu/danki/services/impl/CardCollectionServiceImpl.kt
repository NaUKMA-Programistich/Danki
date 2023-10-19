package ua.ukma.edu.danki.services.impl

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.IllegalAccessException
import ua.ukma.edu.danki.exceptions.ResourceNotFoundException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import java.util.*

class CardCollectionServiceImpl(private val userService: UserService) : CardCollectionService {
    override suspend fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<UserCardCollectionDTO> {
        return getCollectionsOfUser(user, sort, ascending, limit, offset).map { it.toUserCardCollectionDTO() }
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
                mapResultRowToCardCollectionDTO(it)
            }
    }

    private fun mapResultRowToCardCollectionDTO(it: ResultRow): InternalCardCollectionDTO {
        return InternalCardCollectionDTO(
            uuid = it[UserCardCollections.id].value,
            user = it[UserCardCollections.user].value,
            collection = it[CardCollections.id].value,
            favorite = it[UserCardCollections.favorite],
            lastModified = it[CardCollections.lastModified],
            own = it[UserCardCollections.own],
            shared = it[UserCardCollections.shared],
            name = it[CardCollections.name]
        )
    }

    private fun getSortColumn(sort: CollectionSortParam): Column<*> {
        return when (sort) {
            CollectionSortParam.ByName -> CardCollections.name
            CollectionSortParam.ByDate -> CardCollections.lastModified
        }
    }

    override suspend fun removeCollections(user: User, collections: List<UUID>) {
        DatabaseFactory.dbQuery {
            collections.forEach {
                val collection = readCollection(user, it)
                    ?: throw BadRequestException("One or more of the collections requested for deletion could not be found")
                UserCardCollections.deleteWhere { UserCardCollections.id eq collection.uuid }
            }
        }
    }

    override suspend fun updateCollection(cardCollection: InternalCardCollectionDTO) {
        cardCollection.lastModified = Clock.System.now()
        DatabaseFactory.dbQuery {
            val owner = userService.findUser(cardCollection.user)
                ?: throw ResourceNotFoundException("User requesting update of collection was not found")
            val existingCardCollection = CardCollection.findById(cardCollection.collection)
                ?: throw ResourceNotFoundException("Collection for which update was requested was not found")
            val existingUserCardCollection = UserCardCollection.findById(cardCollection.uuid)
                ?: throw ResourceNotFoundException("Collection for which update was requested was not found")
            if (existingUserCardCollection.user.value != owner.id.value)
                throw IllegalAccessException("Users can only modify their own collections")
            if (!existingUserCardCollection.own && cardCollection.shared != existingUserCardCollection.shared)
                throw IllegalAccessException("Users can only share their own collections")
            if (!existingUserCardCollection.own && cardCollection.name != existingCardCollection.name)
                throw IllegalAccessException("Users can only rename their own collections")

            existingCardCollection.name = cardCollection.name
            existingCardCollection.lastModified = cardCollection.lastModified
            existingCardCollection.flush()

            existingUserCardCollection.own = cardCollection.own
            existingUserCardCollection.shared = cardCollection.shared
            existingUserCardCollection.favorite = cardCollection.favorite
            existingUserCardCollection.flush()
        }
    }

    override suspend fun createCollection(email: String, name: String): UUID {
        return createNewCollection(
            name,
            userService.findUser(email)
                ?: throw IllegalAccessException("User trying to create the collection does not exist in the system")
        ).id.value
    }

    override suspend fun readCollection(user: User, collection: UUID): InternalCardCollectionDTO? {
        return DatabaseFactory.dbQuery {
            UserCardCollections
                .innerJoin(CardCollections)
                .select(
                    where = (UserCardCollections.id eq collection)
                        .and
                            (UserCardCollections.user eq user.id)
                )
                .map {
                    mapResultRowToCardCollectionDTO(it)
                }.singleOrNull()
        }
    }

    private suspend fun createNewCollection(nameOfNewCollection: String, userOwner: User): UserCardCollection {
        return DatabaseFactory.dbQuery {
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