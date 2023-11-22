package ua.ukma.edu.danki.data

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import ua.ukma.edu.danki.data.auth.AuthRepositoryImpl

object Injection {
    private val client = HttpClient {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
    }
    val authRepository = AuthRepositoryImpl(client)
}