package ua.ukma.edu.danki.services.impl

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ua.ukma.edu.danki.exceptions.BadRequestException
import ua.ukma.edu.danki.exceptions.IllegalAccessException
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.DatabaseFactory
import java.util.*

class CardCollectionServiceImpl(private val userService: UserService) : CardCollectionService {
    override fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<UserCardCollectionDTO> {
        return runBlocking {
            getCollectionsOfUser(user, sort, ascending, limit, offset).map { it.toUserCardCollectionDTO() }
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

    override fun removeCollections(user: User, collections: List<UUID>) {
        runBlocking {
            DatabaseFactory.dbQuery {
                collections.forEach {
                    val collection = readCollection(user, it)
                        ?: throw BadRequestException("One or more of the collections requested for deletion could not be found")
                    UserCardCollections.deleteWhere { UserCardCollections.id eq collection.uuid }
                }
            }
        }
    }

    override fun updateCollection(user: User, cardCollection: InternalCardCollectionDTO) {
        TODO("Not yet implemented")
    }

    override fun createCollection(email: String, name: String): UUID {
        return runBlocking {
            createNewCollection(
                name,
                userService.findUser(email)
                    ?: throw IllegalAccessException("User trying to create the collection does not exist in the system")
            ).id.value
        }
    }

    override fun readCollection(user: User, collection: UUID): InternalCardCollectionDTO? {
        return runBlocking {
            DatabaseFactory.dbQuery {
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