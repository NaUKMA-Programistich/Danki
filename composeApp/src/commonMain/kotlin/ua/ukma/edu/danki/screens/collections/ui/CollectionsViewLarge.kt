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

@Composable
fun CollectionViewLarge(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Header()
            Row {
                SideNavigation()

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
fun SideNavigation() {
    NavigationRail(modifier = Modifier.padding(top = 44.dp, bottom = 56.dp)) {
        FloatingActionButton(
            onClick = {}
        ) {
            Icon(Icons.Filled.Refresh, "Recents button") // TODO proper recent icon
        }
        Spacer(modifier = Modifier.size(20.dp))
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
fun ExtendedCollectionList(
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
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
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