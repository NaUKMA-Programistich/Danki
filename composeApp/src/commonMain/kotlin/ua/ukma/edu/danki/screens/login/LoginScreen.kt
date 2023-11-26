package ua.ukma.edu.danki.screens.login

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.login.ui.LoginScreenEntry
import ua.ukma.edu.danki.screens.login.viewmodel.LoginAction
import ua.ukma.edu.danki.screens.login.viewmodel.LoginEvent
import ua.ukma.edu.danki.screens.login.viewmodel.LoginState
import ua.ukma.edu.danki.screens.login.viewmodel.LoginViewModel

@Composable
internal fun LoginScreen() {
    StoredViewModel(factory = { LoginViewModel() }) { viewModel ->
        val viewState = viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()
        val navController = LocalRootController.current


        when (val localState = viewState.value) {
            is LoginState.Entry -> {
                LoginScreenEntry(
                    email = localState.email,
                    password = localState.password,
                    onEmailChanged = { viewModel.obtainEvent(LoginEvent.SetEmail(it)) },
                    onPasswordChanged = { viewModel.obtainEvent(LoginEvent.SetPassword(it)) },
                    onLoginClicked = { viewModel.obtainEvent(LoginEvent.Login) },
                    onRegisterClicked = { navController.push(NavigationRoute.Registration.name) }
                )
            }
            LoginState.Loading -> ComposableLoading()
        }

        when (viewAction) {
            is LoginAction.GoToStartScreen -> {
                navController.launch(
                    screen = NavigationRoute.Search.name,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }
            LoginAction.ShowError -> {}
            null -> {}
        }
    }
}
