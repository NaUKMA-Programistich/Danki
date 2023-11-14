package ua.ukma.edu.danki.screens.game_results

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.game_results.ui.GameResultsView
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsAction
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsState
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsViewModel

@Composable
internal fun GameResultsScreen(cards: List<CardDTO>, gameResults: List<Boolean>) {
    StoredViewModel(factory = { GameResultsViewModel(cards, gameResults) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is GameResultsState.ShowGameResults -> GameResultsView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            GameResultsState.Loading -> {}
        }

        when (val action = viewAction) {
            is GameResultsAction.CloseResults -> {
                //TODO navigate back twice
            }

            else -> {

            }
        }
    }
}