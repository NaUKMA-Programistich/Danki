package ua.ukma.edu.danki.utils

interface PasswordEncoder {
    fun encode(password: String): String
    fun isSame(plaintextPassword: String, hashedPassword: String): Boolean
}