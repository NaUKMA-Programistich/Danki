package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.*
import java.util.*


interface CardCollectionService {

    suspend fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<UserCardCollectionDTO>

    suspend fun removeCollections(user: User, collections: List<UUID>)

    suspend fun updateCollection(cardCollection: InternalCardCollectionDTO)

    suspend fun createCollection(email: String, name: String): UUID

    suspend fun readCollection(user: User, collection: UUID): InternalCardCollectionDTO?

}

