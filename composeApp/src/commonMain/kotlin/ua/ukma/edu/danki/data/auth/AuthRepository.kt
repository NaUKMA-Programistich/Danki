package ua.ukma.edu.danki.data.auth

import ua.ukma.edu.danki.models.auth.UserAuthRequest
import ua.ukma.edu.danki.models.auth.UserAuthResponse
import ua.ukma.edu.danki.models.auth.UserRegisterRequest
import ua.ukma.edu.danki.models.auth.UserRegisterResponse

interface AuthRepository {
    suspend fun login(request: UserAuthRequest): UserAuthResponse?
    suspend fun register(request: UserRegisterRequest): UserRegisterResponse?

    suspend fun logout()
}