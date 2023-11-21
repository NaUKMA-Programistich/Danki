package ua.ukma.edu.danki.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.card_collection_viewer.CardCollectionViewerScreen
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel
import ua.ukma.edu.danki.screens.edit_card_screen.EditCardScreen
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import ua.ukma.edu.danki.screens.login.LoginScreen
import ua.ukma.edu.danki.screens.new_cards_viewer.NewCardViewerScreen
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

         screen(NavigationRoute.NewCardViewer.name) {
             NewCardViewerScreen()
         }

         screen(NavigationRoute.CardCollectionViewer.name) {
             CardCollectionViewerScreen()
         }

         screen(NavigationRoute.EditCard.name) {
             EditCardScreen((it as CardViewerModel).run {
                 EditCard(
                     term = this.term,
                     definition = this.definition,
                     collection = this.collection,
                     )
             })
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
  Login, Collections, Game, GameResults, Search, SearchHistory, Definition,EditCard,CardCollectionViewer,NewCardViewer
}
