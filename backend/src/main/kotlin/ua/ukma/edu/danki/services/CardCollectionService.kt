package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.*
import java.util.*


interface CardCollectionService {

    fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean
    ): List<UserCardCollectionDTO>

    fun removeCollections(user: User, collections: List<UUID>)

    fun updateCollection(user: User, cardCollection: InternalCardCollectionDTO)

    fun createCollection(email: String, name: String): UUID

    fun readCollection(user: User, collection: UUID): InternalCardCollectionDTO?

}

