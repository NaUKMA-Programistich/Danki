package ua.ukma.edu.danki.screens.search_history

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
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ua.ukma.edu.danki.screens.search_history.ui.SearchHistoryView
import ua.ukma.edu.danki.screens.search_history.viewmodel.SearchHistoryAction
import ua.ukma.edu.danki.screens.search_history.viewmodel.SearchHistoryState
import ua.ukma.edu.danki.screens.search_history.viewmodel.SearchHistoryViewModel

@Composable
internal fun SearchHistoryScreen() {
    StoredViewModel(factory = { SearchHistoryViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is SearchHistoryState.SearchHistoryList -> SearchHistoryView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) },
            )

            SearchHistoryState.Loading -> {
                // TODO("replace with ComposableLoading from another branch")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(16.dp)
                    )
                }
            }
        }

        when (val action = viewAction) {
            is SearchHistoryAction.OpenDefinition -> {
                // TODO("navigate to word definition screen")
                println("Navigate to '${action.word}' definition")
            }

            is SearchHistoryAction.NavigateBack -> {
                navController.popBackStack()
            }

            null -> {}
        }
    }
}