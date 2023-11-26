package ua.ukma.edu.danki.screens.register.viewmodel

sealed class RegistrationEvent {
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class UsernameChanged(val username: String) : RegistrationEvent()

    object RegisterClicked : RegistrationEvent()
}