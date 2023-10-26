package ua.ukma.edu.danki.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorMsg (
    @SerialName("message")
    val message: String
)
