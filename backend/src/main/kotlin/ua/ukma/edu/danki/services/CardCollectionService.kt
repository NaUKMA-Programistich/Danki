package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.*
import java.util.*


interface CardCollectionService {

    suspend fun getCollections(
        user: User,
        offset: Int,
        limit: Int,
        sort: CollectionSortParam,
        ascending: Boolean,
        favorite: Boolean
    ): List<UserCardCollectionDTO>

    suspend fun getSharedCollection(user: User, id: Long): UserCardCollectionDTO

    suspend fun shareCollection(user: User, collection: UUID): Long

    suspend fun removeCollections(user: User, collections: List<UUID>)

    /**
     * Attempts to update the collection identified by the respective properties of the DTO
     * Throws appropriate exceptions from BadRequest hierarchy if update cannot be done
     * If the user is not the owner of the underlying CardCollection entity, the update to it's name will be skipped
     * and last_modified column will retain it's previous value, otherwise the current system time is set as it's value
     * Only name, shared and favorite properties from the DTO are used to update the db, uuid, user and collection are used
     * to identify the necessary db rows.
     */
    suspend fun updateCollection(cardCollection: InternalCardCollectionDTO)

    suspend fun createCollection(email: String, name: String): UUID

    suspend fun readCollection(user: User, collection: UUID): InternalCardCollectionDTO?

    suspend fun getDefaultCollectionOfUser(user: UUID): InternalCardCollectionDTO?

    suspend fun setLastUpdatedAsNow(id: Long)
}

