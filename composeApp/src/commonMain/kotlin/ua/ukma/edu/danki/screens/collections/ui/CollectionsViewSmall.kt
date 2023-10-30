package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState

@Composable
fun CollectionViewSmall(
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
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FavoriteButton(favoriteOnlyIsOn = state.favoriteOnly, onClick = {
                        if (!state.favoriteOnly) onEvent(CollectionEvent.ShowOnlyFavorites)
                        else onEvent(CollectionEvent.ShowAll)
                    })
                    SortMenu(state, onEvent)
                }

                SmallCollectionList(state, onEvent)
            }
        }
    }
}

@Composable
fun SmallCollectionList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        items(state.collections) { collection ->
            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                CollectionAsItem(collection, onEvent)
            }

        }
    }
}