package ua.ukma.edu.danki.screens.game_results.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsEvent
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsState

@Composable
internal fun GameResultsViewLarge(
    state: GameResultsState.ShowGameResults,
    onEvent: (GameResultsEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(onCloseResults = { onEvent(GameResultsEvent.CloseResults) })
            Row {
                SideNavigation()

                LargeResultsList(state, onEvent)
            }
        }
    }
}

//TODO move side navigation to core/composable?
@Composable
private fun SideNavigation() {
    NavigationRail(modifier = Modifier.padding(top = 44.dp, bottom = 56.dp)) {
        NavigationRailItem(
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Dictionary") }, // TODO proper icon
            label = { Text("Dictionary") },
            selected = false,
            onClick = { }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Collections") }, // TODO proper icon
            label = { Text("Collections") },
            selected = true,
            onClick = { }
        )
    }
}

@Composable
fun LargeResultsList(
    state: GameResultsState.ShowGameResults,
    onEvent: (GameResultsEvent) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.padding(horizontal = 16.dp),
        columns = StaggeredGridCells.FixedSize(size = 304.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(state.gameCardsAndResults) { cardAndResult ->
            var expanded by remember { mutableStateOf(false) }

            ResultAsItem(
                cardAndResult,
                expanded,
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}
