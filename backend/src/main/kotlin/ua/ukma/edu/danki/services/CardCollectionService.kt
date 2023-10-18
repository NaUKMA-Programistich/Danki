package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.exceptions.IllegalAccessException
import ua.ukma.edu.danki.models.CardCollection
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.models.CollectionSortParam
import java.util.UUID


interface CardCollectionService {

    fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<UserCardCollectionDTO>

    @Throws(IllegalAccessException::class)
    fun removeCollections(user: User, collections: List<UUID>)

    fun updateCollection(user: User, collection: CardCollection)

    fun createCollection(email: String, name: String): UUID

    fun readCollection(collection: UUID)

}

