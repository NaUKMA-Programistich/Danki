package ua.ukma.edu.danki.data.card

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ua.ukma.edu.danki.data.API
import ua.ukma.edu.danki.data.Cache
import ua.ukma.edu.danki.models.*

class CardRepositoryImpl(private val client: HttpClient) : CardRepository {
    override suspend fun getCardsOfCollection(request: GetCardsOfCollection): ListOfCardsResponse? {
        return try {
            val url =
                "${API.BASE_URL}/cards?collection=${request.collection}&offset=${request.offset}&limit=${request.limit}&sort=${request.sort}&ascending=${request.ascending}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<ListOfCardsResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createNewCard(request: CardDTO): CardCreatedResponse? {
        return try {
            val url = "${API.BASE_URL}/cards/new"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<CardCreatedResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createCardInCollection(request: CreateCardInCollectionRequest): CardCreatedResponse? {
        return try {
            val url = "${API.BASE_URL}/cards/new/to-collection"
            val result = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<CardCreatedResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCard(request: GetCard): CardDTO? {
        return try {
            val url = "${API.BASE_URL}/card?card=${request.card}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<CardDTO>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteCards(request: DeleteCardsRequest): GenericBooleanResponse? {
        return try {
            val url = "${API.BASE_URL}/cards/delete"
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

    override suspend fun updateCard(request: CardDTO): GenericBooleanResponse? {
        return try {
            val url = "${API.BASE_URL}/cards/update"
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

    override suspend fun moveCardToCollection(request: MoveCardToCollectionRequest): GenericBooleanResponse? {
        return try {
            val url = "${API.BASE_URL}/cards/move"
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
}