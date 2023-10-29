package ua.ukma.edu.danki.navigation

import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.screens.definition.DefinitionScreen
import ua.ukma.edu.danki.screens.login.LoginScreen
import ua.ukma.edu.danki.screens.search.SearchScreen
import ua.ukma.edu.danki.screens.search_history.SearchHistoryScreen

internal fun RootComposeBuilder.NavigationGraph() {

    screen(NavigationRoute.Login.name) {
        LoginScreen()
    }
    screen(NavigationRoute.Search.name) {
        SearchScreen()
    }
    screen(NavigationRoute.SearchHistory.name) {
        SearchHistoryScreen()
    }
    screen(NavigationRoute.Definition.name) {
        DefinitionScreen(term = it as String)
    }

}

internal enum class NavigationRoute {
    Login, Search, SearchHistory, Definition
}
