package ua.ukma.edu.danki.screens.register.viewmodel

sealed class RegistrationState {
    data object Loading : RegistrationState()
    data class Entry(val email: String, val password: String, val username: String) : RegistrationState()
}