package ua.ukma.edu.danki.screens.game_results.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsEvent
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsState

@Composable
internal fun GameResultsViewSmall(
    state: GameResultsState.ShowGameResults,
    onEvent: (GameResultsEvent) -> Unit
) {
    Scaffold(
        topBar = { Header(onCloseResults = { onEvent(GameResultsEvent.CloseResults) }) },
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.gameCardsAndResults) { cardAndResult ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = MaterialTheme.shapes.large,
                        onClick = { onEvent(GameResultsEvent.ShowDefinition(cardAndResult.first)) }
                    ) {
                        ResultAsItem(cardAndResult)
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}
