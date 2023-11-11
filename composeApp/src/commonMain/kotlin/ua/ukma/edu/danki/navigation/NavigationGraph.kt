package ua.ukma.edu.danki.navigation

import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.screens.game.GameScreen
import ua.ukma.edu.danki.screens.login.LoginScreen

internal fun RootComposeBuilder.NavigationGraph() {

    screen(NavigationRoute.Login.name) {
        LoginScreen()
    }
    screen(NavigationRoute.Login.name) {
        GameScreen(collectionId = it as String)
    }
}

internal enum class NavigationRoute {
    Login, Game
}
