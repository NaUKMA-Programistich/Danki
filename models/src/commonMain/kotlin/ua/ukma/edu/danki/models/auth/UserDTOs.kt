package ua.ukma.edu.danki.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class UserRegisterRequest(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class UserAuthResponse(
    @SerialName("jwt")
    val jwt: String
)

@Serializable
data class UserRegisterResponse(
    @SerialName("success")
    val success: Boolean
)