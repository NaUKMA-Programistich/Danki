package ua.ukma.edu.danki.screens.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.register.ui.RegistrationScreenEntry
import ua.ukma.edu.danki.screens.register.viewmodel.RegistrationAction
import ua.ukma.edu.danki.screens.register.viewmodel.RegistrationEvent
import ua.ukma.edu.danki.screens.register.viewmodel.RegistrationState
import ua.ukma.edu.danki.screens.register.viewmodel.RegistrationViewModel

@Composable
internal fun RegistrationScreen() {
    StoredViewModel(factory = { RegistrationViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is RegistrationState.Entry -> {
                RegistrationScreenEntry(
                    email = state.email,
                    password = state.password,
                    username = state.username,
                    onEmailChanged = { viewModel.obtainEvent(RegistrationEvent.EmailChanged(it)) },
                    onPasswordChanged = { viewModel.obtainEvent(RegistrationEvent.PasswordChanged(it)) },
                    onUsernameChanged = { viewModel.obtainEvent(RegistrationEvent.UsernameChanged(it)) },
                    onRegisterClicked = { viewModel.obtainEvent(RegistrationEvent.RegisterClicked) }
                )
            }
            RegistrationState.Loading -> ComposableLoading()
        }

        when (val action = viewAction) {
            RegistrationAction.NavigateToLogin -> {
                navController.launch(
                    screen = NavigationRoute.Login.name,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }
            is RegistrationAction.ShowError -> {}
            null -> {}
        }
    }
}
