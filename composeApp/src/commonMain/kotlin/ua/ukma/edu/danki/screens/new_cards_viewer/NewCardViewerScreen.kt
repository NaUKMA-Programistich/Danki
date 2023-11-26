package ua.ukma.edu.danki.screens.new_cards_viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.new_cards_viewer.ui.AddAllDialog
import ua.ukma.edu.danki.screens.new_cards_viewer.ui.NewCardViewerView
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerAction
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerEvent
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerState
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerViewModel


@Composable
fun NewCardViewerScreen() {
    StoredViewModel(factory = { NewCardViewerViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val modalController = navController.findModalController()
        val alertConfiguration = AlertConfiguration(maxHeight = 0.45f, maxWidth = 0.6f, cornerRadius = 8)
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is NewCardViewerState.NewCardCards -> {
                NewCardViewerView(
                    state = state,
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            is NewCardViewerState.Loading -> {
                ComposableLoading()
            }
        }

        when (val action = viewAction) {
            is NewCardViewerAction.NavigateBack -> {
                navController.popBackStack()
            }

            is NewCardViewerAction.OpenCardToEdit -> {
                navController.launch(
                    screen = NavigationRoute.EditCard.name,
                    params = action.newCard,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }

            is NewCardViewerAction.ShowAddAllDialog -> {
                modalController.present(alertConfiguration) { key ->
                    AddAllDialog(
                        collections = action.collections,
                        addAll = { collection ->
                            viewModel.obtainEvent(NewCardViewerEvent.AddAllRecentsToCollection(collection))
                            modalController.popBackStack(key)
                        }
                    )
                }
            }

            else -> {

            }
        }
    }
}
