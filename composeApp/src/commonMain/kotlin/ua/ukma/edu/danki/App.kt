package ua.ukma.edu.danki

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexgladkov.odyssey.compose.extensions.customNavigation
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.compose.setup.setNavigationContent
import ua.ukma.edu.danki.navigation.NavigationGraph
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.collections.ui.FavoriteAndSortButtonsRow
import ua.ukma.edu.danki.theme.AppTheme



@Composable
internal fun App(
    systemAppearance: (isLight: Boolean) -> Unit = {},
    odysseyConfiguration: OdysseyConfiguration
) = AppTheme(systemAppearance) {
    //val animatedContent: AnimatedContentTransitionScopeImpl
    //var email by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }

    setNavigationContent(odysseyConfiguration) {
        NavigationGraph()
    }

}


internal expect fun openUrl(url: String?)