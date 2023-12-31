package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.UserCardCollectionDTO
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
            Header(onEvent)
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
            var menuExpanded by remember { mutableStateOf(false) }

            Box {
                CollectionAsSurface(collection, state, { menuExpanded = true }, onEvent)
                CollectionMenu(collection, menuExpanded, { menuExpanded = false }, onEvent)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectionAsSurface(
    collection: UserCardCollectionDTO,
    state: CollectionState.CollectionList,
    onOpenMenu: () -> Unit,
    onEvent: (CollectionEvent) -> Unit
) {
    var collectionSelected by mutableStateOf(state.selected.contains(collection.id))

    Surface(
        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.large).combinedClickable(
            onClick = {
                if (state.selectionMode) {
                    onEvent(CollectionEvent.ToggleSelectCollection(collection.id))
                    collectionSelected = state.selected.contains(collection.id)
                } else onEvent(CollectionEvent.OpenCollection(collection))
            },
            onLongClick = onOpenMenu
        ),
        color = if (collectionSelected) MaterialTheme.colorScheme.outlineVariant
        else MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            CollectionAsItem(collection, onEvent)
        }
    }
}