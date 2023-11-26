package ua.ukma.edu.danki.screens.login.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Cache
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.auth.AuthRepository
import ua.ukma.edu.danki.models.auth.UserAuthRequest


sealed interface LoginState {
    data class Entry(val email: String, val password: String) : LoginState
    data object Loading : LoginState
}

sealed interface LoginEvent {
    data class SetEmail(val email: String) : LoginEvent
    data class SetPassword(val password: String) : LoginEvent
    data object Login : LoginEvent
}

sealed interface LoginAction {
    data object GoToStartScreen : LoginAction
    data object ShowError : LoginAction
}


class LoginViewModel(
    private val authRepository: AuthRepository = Injection.authRepository
) : ViewModel<LoginState, LoginAction, LoginEvent>(LoginState.Loading) {

    init {
        withViewModelScope {
            if (Cache.jwt.isEmpty()) {
                setViewState(LoginState.Entry("", ""))
            } else {
                setViewAction(LoginAction.GoToStartScreen)
            }
        }
    }

    override fun obtainEvent(viewEvent: LoginEvent) {
        val localState = viewStates().value

        when (viewEvent) {
            is LoginEvent.SetEmail -> withViewModelScope {
                if (localState !is LoginState.Entry) return@withViewModelScope
                setViewState(localState.copy(email = viewEvent.email))
            }

            is LoginEvent.SetPassword -> withViewModelScope {
                if (localState !is LoginState.Entry) return@withViewModelScope
                setViewState(localState.copy(password = viewEvent.password))
            }

            is LoginEvent.Login -> withViewModelScope {
                if (localState !is LoginState.Entry) return@withViewModelScope

                setViewState(LoginState.Loading)
                val response = authRepository.login(
                    UserAuthRequest(
                        email = localState.email,
                        password = localState.password
                    )
                )
                if (response != null) {
                    setViewAction(LoginAction.GoToStartScreen)
                } else {
                    setViewAction(LoginAction.ShowError)
                    setViewState(LoginState.Entry(localState.email, localState.password))
                }
            }
        }
    }
}