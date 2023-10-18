package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.CardCollection
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.UserCardCollectionDTO
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

    fun updateCollection(user: User, collection: CardCollection)

    fun createCollection(email: String, name: String): UUID

    fun readCollection(user: User, collection: UUID): UserCardCollectionDTO?

}

