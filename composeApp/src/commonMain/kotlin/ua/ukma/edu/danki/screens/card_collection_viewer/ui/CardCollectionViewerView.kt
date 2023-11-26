package ua.ukma.edu.danki.screens.card_collection_viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.CardSortParam
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerEvent
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerState
import ua.ukma.edu.danki.screens.collections.ui.paramToString
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState

@Composable
fun CardCollectionViewerView(
    state: CardCollectionViewerState.CollectionCards,
    onEvent: (CardCollectionViewerEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = { PlayFAB(onEvent = onEvent) }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(16.dp).padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                HeaderComponent(
                    collectionName = state.collection.name,
                    onBack = { onEvent(CardCollectionViewerEvent.GoBack) },
                    onShare = { onEvent(CardCollectionViewerEvent.ShareCollection) },
                    onDelete = { onEvent(CardCollectionViewerEvent.DeleteCollection(collection = state.collection)) })

                FavoriteAndSortButtonsRow(state = state, onEvent = onEvent)
                CardList(
                    cardList = state.cardList,
                    onCard = { onEvent(CardCollectionViewerEvent.OnCardClick(card = it)) })
            }
        }
    }
}

@Composable
private fun HeaderComponent(
    onBack: () -> Unit,
    onDelete: () -> Unit = {},
    onShare: () -> Unit = {},
    collectionName: String
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            IconButton(modifier = Modifier, onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box(modifier = Modifier.weight(8f), contentAlignment = Alignment.CenterStart) {
            Text(
                modifier = Modifier.offset(y = (-2).dp),
                text = collectionName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        onDelete()
                        menuExpanded = false
                    },
                    text = { Text("Delete") }
                )
                DropdownMenuItem(
                    onClick = {
                        onShare()
                        menuExpanded = false
                    },
                    text = { Text("Share") }
                )
            }
        }
    }
}

@Composable
private fun CardList(
    cardList: List<CardDTO>,
    onCard: (CardDTO) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(16.dp).clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        cardList.forEach { card ->
            item {
                CardComponent(card = card, onCard = onCard)
            }
        }
    }
}

@Composable
private fun CardComponent(
    card: CardDTO,
    onCard: (CardDTO) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clip(MaterialTheme.shapes.large).clickable { onCard(card) }
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier,
            text = card.term,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier,
            text = card.definition,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
private fun FavoriteAndSortButtonsRow(
    state:CardCollectionViewerState.CollectionCards,
    onEvent: (CardCollectionViewerEvent) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        SortMenu(state, onEvent)
        OrderButton(state.orderIsAscending, onClick = { onEvent(CardCollectionViewerEvent.ChangeSortingOrder(!state.orderIsAscending)) })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortMenu(
    state: CardCollectionViewerState.CollectionCards,
    onEvent: (CardCollectionViewerEvent) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        ElevatedFilterChip(
            onClick = { menuExpanded = true },
            shape = MaterialTheme.shapes.medium,
            label = { Text(state.sortingParam.paramToString(), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = false,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "arrow down icon") },
        )
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            enumValues<CardSortParam>().forEach { sortParam ->
                DropdownMenuItem(
                    onClick = {
                        onEvent(CardCollectionViewerEvent.SortList(sortParam))
                        menuExpanded = false
                    },
                    text = { Text(sortParam.paramToString()) },
                    enabled = state.sortingParam != sortParam,
                )
            }
        }
    }
}

@Composable
private fun OrderButton(
    isAscending: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(checked = isAscending, onCheckedChange = { onClick() }) {
        Icon(
            if (isAscending) Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown,
            "ascending order is $isAscending"
        )
    }
}

fun CardSortParam.paramToString(): String {
    return when (this) {
        CardSortParam.ByLastModified -> {
            "By last modified"
        }
        CardSortParam.ByTimeAdded -> {
            "By time added"
        }
        CardSortParam.ByTerm -> {
            "By term"
        }
        else  -> { "..." }
    }
}



@Composable
private fun PlayFAB(onEvent: (CardCollectionViewerEvent) -> Unit) {
    FloatingActionButton(
        onClick = { onEvent(CardCollectionViewerEvent.PlayGame) }
    ) {
        Text("Play")
    }
}

