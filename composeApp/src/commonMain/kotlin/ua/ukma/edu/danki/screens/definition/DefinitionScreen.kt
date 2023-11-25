package ua.ukma.edu.danki.screens.definition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.definition.ui.TermDefinitionView
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionAction
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionState
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionViewModel
import ua.ukma.edu.danki.screens.game.viewmodel.GameState

@Composable
internal fun DefinitionScreen(term: String) {
    StoredViewModel(factory = { DefinitionViewModel(term) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is DefinitionState.TermDefinition -> TermDefinitionView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            is DefinitionState.Loading -> {
                ComposableLoading()
            }
        }

        when (val action = viewAction) {
            is DefinitionAction.NavigateBack -> {
                navController.popBackStack()
            }

            is DefinitionAction.OnNewCard -> {
                navController.launch(
                    screen = NavigationRoute.AddCard.name,
                    params = action.newCard,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }

            null -> {}
        }
    }
}