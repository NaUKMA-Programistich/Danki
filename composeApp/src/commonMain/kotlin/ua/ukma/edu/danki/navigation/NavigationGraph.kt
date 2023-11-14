package ua.ukma.edu.danki.navigation

import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.screens.definition.DefinitionScreen
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.game.GameScreen
import ua.ukma.edu.danki.screens.game_results.GameResultsScreen
import ua.ukma.edu.danki.screens.game.GameScreen
import ua.ukma.edu.danki.screens.collections.CollectionsScreen
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

    screen(NavigationRoute.Collections.name) {
        CollectionsScreen()
    }

    screen(NavigationRoute.Game.name) {
        GameScreen(collectionId = it as String)
    }

    screen(NavigationRoute.GameResults.name) { cardsAndResults ->
        if (cardsAndResults !is Pair<*, *>) return@screen
        GameResultsScreen(
            cards = (cardsAndResults.first as List<CardDTO>),
            gameResults = cardsAndResults.second as List<Boolean>
        )
    }
}

internal enum class NavigationRoute {
    Login, Collections, Game, GameResults, Search, SearchHistory, Definition
}
