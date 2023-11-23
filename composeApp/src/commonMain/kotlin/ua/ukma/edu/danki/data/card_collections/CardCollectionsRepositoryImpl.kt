package ua.ukma.edu.danki.data.card_collections

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ua.ukma.edu.danki.data.API
import ua.ukma.edu.danki.data.Cache
import ua.ukma.edu.danki.models.*

class CardCollectionsRepositoryImpl(private val client: HttpClient) : CardCollectionsRepository {
    override suspend fun getUserCollections(request: GetUserCollections): ListOfCollectionsResponse? {
        return try {
            val url =
                "${API.BASE_URL}/collections?offset=${request.offset}&limit=${request.limit}&sort=${request.sort}&ascending=${request.ascending}&favorite=${request.favorite}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<ListOfCollectionsResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getRecentCollection(): UserCardCollectionDTO? {
        return try {
            val url = "${API.BASE_URL}/collections/recents"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<UserCardCollectionDTO>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createCardCollection(request: CreateCardCollectionRequest): CreateCardCollectionResponse? {
        return try {
            val url = "${API.BASE_URL}/collections"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<CreateCardCollectionResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateCollection(request: UpdateCollectionRequest): GenericBooleanResponse? {
        return try {
            val url = "${API.BASE_URL}/collections/"
            val result = client.put(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<GenericBooleanResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteCollection(request: DeleteCollectionsRequest): GenericBooleanResponse? {
        return try {
            val url = "${API.BASE_URL}/delete"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<GenericBooleanResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun shareCollection(request: ShareCollectionRequest): ShareCollectionResponse? {
        return try {
            val url = "${API.BASE_URL}/share"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<ShareCollectionResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getSharedCollection(request: ReadSharedCollectionRequest): UserCardCollectionDTO? {
        return try {
            val url = "${API.BASE_URL}/get-shared"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<UserCardCollectionDTO>()
        } catch (e: Exception) {
            null
        }
    }
}