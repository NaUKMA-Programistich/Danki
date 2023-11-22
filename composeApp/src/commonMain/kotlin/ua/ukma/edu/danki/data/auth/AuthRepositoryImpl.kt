package ua.ukma.edu.danki.data.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ua.ukma.edu.danki.data.API
import ua.ukma.edu.danki.models.auth.UserAuthRequest
import ua.ukma.edu.danki.models.auth.UserAuthResponse
import ua.ukma.edu.danki.models.auth.UserRegisterRequest
import ua.ukma.edu.danki.models.auth.UserRegisterResponse

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {

    override suspend fun login(request: UserAuthRequest): UserAuthResponse? {
        return try {
            val result = client.post("${API.BASE_URL}/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            result.body<UserAuthResponse>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun register(request: UserRegisterRequest): UserRegisterResponse? {
        return try {
            val result = client.post("${API.BASE_URL}/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            result.body<UserRegisterResponse>()
        } catch (e: Exception) {
            null
        }
    }
}