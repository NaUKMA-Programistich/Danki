package ua.ukma.edu.danki.screens.register.viewmodel

sealed class RegistrationAction {
    data object NavigateToLogin : RegistrationAction()
    data object ShowError : RegistrationAction()
}