package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable




@Serializable
data class SimpleDto(
    @SerialName("test")
    val test: String = "test"
)