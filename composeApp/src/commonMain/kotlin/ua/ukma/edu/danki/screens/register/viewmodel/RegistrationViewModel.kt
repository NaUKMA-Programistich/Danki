package ua.ukma.edu.danki.screens.register.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.auth.AuthRepository
import ua.ukma.edu.danki.models.auth.UserRegisterRequest

class RegistrationViewModel (
    private val authRepository: AuthRepository = Injection.authRepository
) : ViewModel<RegistrationState, RegistrationAction, RegistrationEvent>(
    initialState = RegistrationState.Entry("", "", "")
) {
    override fun obtainEvent(viewEvent: RegistrationEvent) {
        val localState = viewStates().value

        when (viewEvent) {
            is RegistrationEvent.EmailChanged -> withViewModelScope {
                if (localState !is RegistrationState.Entry) return@withViewModelScope
                setViewState(localState.copy(email = viewEvent.email))
            }
            is RegistrationEvent.PasswordChanged -> withViewModelScope {
                if (localState !is RegistrationState.Entry) return@withViewModelScope
                setViewState(localState.copy(password = viewEvent.password))
            }
            is RegistrationEvent.UsernameChanged -> withViewModelScope {
                if (localState !is RegistrationState.Entry) return@withViewModelScope
                setViewState(localState.copy(username = viewEvent.username))
            }
            RegistrationEvent.RegisterClicked -> withViewModelScope {
                processRegistration(localState as? RegistrationState.Entry ?: return@withViewModelScope)
            }
        }
    }

    private suspend fun processRegistration(
        state: RegistrationState.Entry
    ) {
        setViewState(RegistrationState.Loading)

        val request = UserRegisterRequest(
            email = state.email,
            password = state.password,
            username = state.username
        )

        val response = authRepository.register(request)
        when {
            response == null || response.success.not() -> {
                setViewState(RegistrationState.Entry(state.email, state.password, state.username))
                setViewAction(RegistrationAction.ShowError)
            }
            else -> {
                setViewAction(RegistrationAction.NavigateToLogin)
            }
        }
    }
}