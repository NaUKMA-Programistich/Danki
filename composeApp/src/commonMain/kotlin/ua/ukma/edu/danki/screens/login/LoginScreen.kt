package ua.ukma.edu.danki.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.SimpleDto
import ua.ukma.edu.danki.rememberDarkMode
import ua.ukma.edu.danki.rememberLightMode
import ua.ukma.edu.danki.theme.LocalThemeIsDark


data class AppState(
    val email: String,
    val password: String
);

sealed interface AppEvent {
    data class SetEmail(val email: String) : AppEvent
    data class SetPassword(val password: String) : AppEvent
}

class AppViewModel() : ViewModel<AppState, Unit, AppEvent>(
    AppState("", "")
) {

    override fun obtainEvent(viewEvent: AppEvent) {
        when (viewEvent) {
            is AppEvent.SetEmail -> withViewModelScope {
                setViewState(viewStates().value.copy(email = viewEvent.email))
            }

            is AppEvent.SetPassword -> withViewModelScope {
                setViewState(viewStates().value.copy(password = viewEvent.password))
            }
        }
    }
}


@Composable
internal fun LoginScreen() {
    StoredViewModel(factory = { AppViewModel() }) { viewModel ->

        val viewState = viewModel.viewStates().observeAsState()
        var passwordVisibility by remember { mutableStateOf(false) }


        val dto by remember { mutableStateOf(SimpleDto(test = "test")) }

        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login " + dto.test,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.weight(1.0f))

                var isDark by LocalThemeIsDark.current
                IconButton(
                    onClick = { isDark = !isDark }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp).size(20.dp),
                        imageVector = if (isDark) rememberLightMode() else rememberDarkMode(),
                        contentDescription = null
                    )
                }
            }

            OutlinedTextField(
                value = viewState.value.email,
                onValueChange = { viewModel.obtainEvent(AppEvent.SetEmail(it)) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            OutlinedTextField(
                value = viewState.value.password,
                onValueChange = { viewModel.obtainEvent(AppEvent.SetPassword(it)) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        val imageVector = if (passwordVisibility) Icons.Default.Close else Icons.Default.Edit
                        Icon(
                            imageVector,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Button(
                onClick = { /* Handle login logic here */ },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Login")
            }

            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Open github")
            }

        }
    }
}