package ua.ukma.edu.danki.data

import com.russhwolf.settings.Settings

object Cache {
    private val settings = Settings()

    var jwt: String
        get() = settings.getString(JWT, "")
        set(value) = settings.putString(JWT, value)

    private const val JWT = "jwt"
}