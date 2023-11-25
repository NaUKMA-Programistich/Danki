package ua.ukma.edu.danki.screens.card_collection_viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.card_collection_viewer.ui.CardCollectionViewerView
import ua.ukma.edu.danki.screens.card_collection_viewer.ui.SharedCodeDialog
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerAction
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerState
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerViewModel

@Composable
fun CardCollectionViewerScreen(
    collection: UserCardCollectionDTO
) {
    StoredViewModel(factory = { CardCollectionViewerViewModel(collection = collection) }) { viewModel ->
        val navController = LocalRootController.current
        val modalController = navController.findModalController()
        val alertConfiguration = AlertConfiguration(maxHeight = 0.25f, maxWidth = 0.4f, cornerRadius = 8)
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()


        when (val state = viewState) {
            is CardCollectionViewerState.CollectionCards -> {
                CardCollectionViewerView(
                    state = state,
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            is CardCollectionViewerState.Loading -> {
                ComposableLoading()
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

            is CardCollectionViewerAction.PlayGame -> {
                navController.launch(
                    screen = NavigationRoute.Game.name,
                    params = action.collectionId,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }

            is CardCollectionViewerAction.ShowSharedCode -> {
                modalController.present(alertConfiguration) {
                    SharedCodeDialog(action.code)
                }
            }

            else -> {

            }
        }
    }
}
