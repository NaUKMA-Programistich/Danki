package ua.ukma.edu.danki.data.card_collections

import ua.ukma.edu.danki.models.*

interface CardCollectionsRepository {
    suspend fun getUserCollections(request: GetUserCollections): ListOfCollectionsResponse?
    suspend fun getRecentCollection(): UserCardCollectionDTO?
    suspend fun createCardCollection(request: CreateCardCollectionRequest): CreateCardCollectionResponse?
    suspend fun updateCollection(request: UpdateCollectionRequest): GenericBooleanResponse?
    suspend fun deleteCollection(request: DeleteCollectionsRequest): GenericBooleanResponse?
    suspend fun shareCollection(request: ShareCollectionRequest): ShareCollectionResponse?
    suspend fun getSharedCollection(request: ReadSharedCollectionRequest): UserCardCollectionDTO?
}