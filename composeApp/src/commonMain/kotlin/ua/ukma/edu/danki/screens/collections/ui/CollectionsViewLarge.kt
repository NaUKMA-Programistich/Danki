package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.theme.surfaceContainer

@Composable
internal fun CollectionViewLarge(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Header()
            Row {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    FavoriteAndSortButtonsRow(state, onEvent)
                    ExtendedCollectionList(state, onEvent)
                }
            }
        }
    }
}

//TODO move side navigation to core/composable?


@Composable
private fun ExtendedCollectionList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 16.dp),
        columns = GridCells.FixedSize(size = 304.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.collections) { collection ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
                onClick = { onEvent(CollectionEvent.OpenCollection(collection)) }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    CollectionAsItem(collection, onEvent)
                }
            }
        }
    }
}