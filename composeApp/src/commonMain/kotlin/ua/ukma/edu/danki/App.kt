package ua.ukma.edu.danki

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
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.StartScreen
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.SimpleDto
import ua.ukma.edu.danki.navigation.NavigationGraph
import ua.ukma.edu.danki.screens.login.LoginScreen
import ua.ukma.edu.danki.theme.AppTheme
import ua.ukma.edu.danki.theme.LocalThemeIsDark
import ua.ukma.edu.danki.theme.mColors
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color


@Composable
internal fun App(
    systemAppearance: (isLight: Boolean) -> Unit = {},
    odysseyConfiguration: OdysseyConfiguration
) = AppTheme(systemAppearance) {
    //val animatedContent: AnimatedContentTransitionScopeImpl
    //var email by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }

    Scaffold {
        Text("Hello there", modifier = Modifier.fillMaxSize())
    }


    setNavigationContent(odysseyConfiguration) {
        NavigationGraph()
    }

}

internal expect fun openUrl(url: String?)