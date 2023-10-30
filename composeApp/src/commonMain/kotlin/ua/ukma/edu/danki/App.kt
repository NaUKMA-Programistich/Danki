package ua.ukma.edu.danki

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import ua.ukma.edu.danki.navigation.NavigationGraph
import ua.ukma.edu.danki.theme.AppTheme



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