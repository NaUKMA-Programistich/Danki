package ua.ukma.edu.danki.screens.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.game.ui.GameView
import ua.ukma.edu.danki.screens.game.viewmodel.GameAction
import ua.ukma.edu.danki.screens.game.viewmodel.GameState
import ua.ukma.edu.danki.screens.game.viewmodel.GameViewModel

@Composable
internal fun GameScreen(collectionId: String) {
    StoredViewModel(factory = { GameViewModel(collectionId) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is GameState.GameInProgress -> GameView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            GameState.Loading -> {
                ComposableLoading()
            }
        }

        when (val action = viewAction) {
            is GameAction.ShowGameResult -> {
                navController.launch(
                    screen = NavigationRoute.GameResults.name,
                    params = action.gameCards to action.gameResults,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }

            else -> {

            }
        }
    }
}