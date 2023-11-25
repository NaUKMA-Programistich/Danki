package ua.ukma.edu.danki.screens.card_collection_viewer

import androidx.compose.runtime.*
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.card_collection_viewer.ui.CardCollectionViewerView
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerAction
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerState
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerViewModel

@Composable
fun CardCollectionViewerScreen (
    collection: UserCardCollectionDTO
) {
    StoredViewModel(factory = { CardCollectionViewerViewModel(collection = collection) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is CardCollectionViewerState.CollectionCards -> {
                CardCollectionViewerView (
                    state = state,
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            is CardCollectionViewerState.Loading -> {

            }
        }

        when (val action = viewAction) {
            is CardCollectionViewerAction.NavigateBack -> {
                navController.popBackStack()
            }
            is CardCollectionViewerAction.OpenCardToEdit -> {
                navController.launch(
                    screen = NavigationRoute.EditCard.name,
                    params = action.card,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }
            else -> {

            }
        }
    }
}
