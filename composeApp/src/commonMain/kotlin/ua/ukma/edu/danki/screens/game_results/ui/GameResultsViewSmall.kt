package ua.ukma.edu.danki.screens.game_results.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
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
                modifier = Modifier.padding(innerPadding).padding(horizontal = 19.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.gameCardsAndResults) { cardAndResult ->
                    var expanded by remember { mutableStateOf(false) }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = MaterialTheme.shapes.large,
                        onClick = { expanded = !expanded }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                        ) {
                            ResultAsItem(cardAndResult, expanded)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}
