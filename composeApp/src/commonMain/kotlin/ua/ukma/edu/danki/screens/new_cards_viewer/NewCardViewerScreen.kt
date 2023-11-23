package ua.ukma.edu.danki.screens.new_cards_viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.new_cards_viewer.ui.NewCardViewerView
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerAction
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerState
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerViewModel


@Composable
fun NewCardViewerScreen () {
    StoredViewModel(factory = { NewCardViewerViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is NewCardViewerState.NewCardCards -> {
                NewCardViewerView (
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
            else -> {

            }
        }
    }
}
