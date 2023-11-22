package ua.ukma.edu.danki.data.dictionary

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ua.ukma.edu.danki.data.API
import ua.ukma.edu.danki.data.Cache
import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition
import ua.ukma.edu.danki.models.dictionary.TermDefinition

class DictionaryRepositoryImpl(private val client: HttpClient) : DictionaryRepository {
    override suspend fun getDictionarySuggestion(request: GetDictionarySuggestions): DictionarySuggestions? {
        return try {
            val url = "${API.BASE_URL}/dictionary/suggest/${request.input}?count=${request.count}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<DictionarySuggestions>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getTermDefinition(request: GetTermDefinition): TermDefinition? {
        return try {
            val url = "${API.BASE_URL}/dictionary/term/${request.term}"
            val result = client.get(url) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer ${Cache.jwt}")
                }
            }
            result.body<TermDefinition>()
        } catch (e: Exception) {
            null
        }
    }
}