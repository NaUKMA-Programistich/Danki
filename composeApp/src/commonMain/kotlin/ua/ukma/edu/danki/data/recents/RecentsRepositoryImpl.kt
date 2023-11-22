package ua.ukma.edu.danki.data.recents

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ua.ukma.edu.danki.data.API
import ua.ukma.edu.danki.data.Cache
import ua.ukma.edu.danki.models.GetRecentTerms
import ua.ukma.edu.danki.models.RecentTerms

class RecentsRepositoryImpl(private val client: HttpClient) : RecentsRepository {
    override suspend fun getRecentTerms(request: GetRecentTerms): RecentTerms? {
        return try {
            val url = "${API.BASE_URL}/recents?offset=${request.offset}&limit=${request.limit}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<RecentTerms>()
        } catch (e: Exception) {
            null
        }
    }
}