package ua.ukma.edu.danki.data

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import ua.ukma.edu.danki.data.auth.AuthRepositoryImpl
import ua.ukma.edu.danki.data.card.CardRepositoryImpl
import ua.ukma.edu.danki.data.dictionary.DictionaryRepositoryImpl
import ua.ukma.edu.danki.data.recents.RecentsRepositoryImpl

object Injection {
    private val client = HttpClient {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
    }
    val authRepository = AuthRepositoryImpl(client)
    val dictionaryRepository = DictionaryRepositoryImpl(client)
    val recentsRepository = RecentsRepositoryImpl(client)
    val cardRepository = CardRepositoryImpl(client)
}