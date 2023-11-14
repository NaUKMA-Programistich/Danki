package ua.ukma.edu.danki.screens.search

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
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.search.ui.SearchResultsView
import ua.ukma.edu.danki.screens.search.viewmodel.SearchAction
import ua.ukma.edu.danki.screens.search.viewmodel.SearchState
import ua.ukma.edu.danki.screens.search.viewmodel.SearchViewModel

@Composable
internal fun SearchScreen() {
    StoredViewModel(factory = { SearchViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is SearchState.SearchResults -> SearchResultsView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            SearchState.Loading -> {
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
            is SearchAction.OpenHistory -> {
                navController.push(NavigationRoute.SearchHistory.name)
            }

            is SearchAction.OpenDefinition -> {
                navController.push(NavigationRoute.Definition.name, action.word)
            }

            null -> {}
        }
    }
}