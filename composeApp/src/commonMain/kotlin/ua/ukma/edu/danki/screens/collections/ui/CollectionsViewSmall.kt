package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState

@Composable
internal fun CollectionViewSmall(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    Scaffold(
        topBar = { Header() },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {}) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Refresh, "Recents")
                    Text("Recents")
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(innerPadding).padding(horizontal = 32.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FavoriteAndSortButtonsRow(state, onEvent)
                SmallCollectionList(state, onEvent)
            }
        }
    }
}

@Composable
private fun SmallCollectionList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        items(state.collections) { collection ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.large,
                onClick = { onEvent(CollectionEvent.OpenCollection(collection)) }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    CollectionAsItem(collection, onEvent)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.size(56.dp))
        }
    }
}