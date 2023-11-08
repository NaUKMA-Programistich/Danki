package ua.ukma.edu.danki.navigation

import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.card_collection_viewer.CardCollectionViewerScreen
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel
import ua.ukma.edu.danki.screens.edit_card_screen.EditCardScreen
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import ua.ukma.edu.danki.screens.login.LoginScreen
import ua.ukma.edu.danki.screens.new_cards_viewer.NewCardViewerScreen


internal fun RootComposeBuilder.NavigationGraph() {

    /*screen(NavigationRoute.Login.name) {
        LoginScreen()
    }*/

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

}

internal enum class NavigationRoute {
    Login,EditCard,CardCollectionViewer,NewCardViewer
}
