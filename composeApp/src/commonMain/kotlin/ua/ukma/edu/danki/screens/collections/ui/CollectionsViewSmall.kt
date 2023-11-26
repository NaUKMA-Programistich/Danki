package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.theme.surfaceContainerHighest

@Composable
internal fun CollectionViewSmall(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    Scaffold(
        topBar = { Header(onEvent) },
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
            var menuExpanded by remember { mutableStateOf(false) }

            Box {
                CollectionAsSurface(collection, state, { menuExpanded = true }, onEvent)
                CollectionMenu(collection, menuExpanded, { menuExpanded = false }, onEvent)
            }
        }
        item {
            Spacer(modifier = Modifier.size(56.dp))
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
        color = if (collectionSelected) MaterialTheme.colorScheme.surfaceContainerHighest
        else MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            CollectionAsItem(collection, onEvent)
        }
    }
}