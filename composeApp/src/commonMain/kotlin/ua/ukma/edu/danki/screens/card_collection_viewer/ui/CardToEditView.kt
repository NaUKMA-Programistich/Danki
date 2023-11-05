package ua.ukma.edu.danki.screens.card_collection_viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerEvent
import ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel.CardCollectionViewerState
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard

@Composable
fun CardCollectionViewerView (
    state: CardCollectionViewerState.CollectionCards,
    onEvent: (CardCollectionViewerEvent) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderComponent(
                collectionName = state.cardCollectionViewerModel.name,
                onBack = { onEvent(CardCollectionViewerEvent.GoBack) },
                onDelete = { onEvent(CardCollectionViewerEvent.DeleteCollection(cardCollectionViewerModel = state.cardCollectionViewerModel)) })
            CardList(cardList = state.cardViewerModelList , onCard = { onEvent(CardCollectionViewerEvent.OnCardClick(cardViewerModel = it)) })
        }
    }
}

@Composable
private fun HeaderComponent (onBack: () -> Unit, onDelete: () -> Unit = {}, collectionName: String) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box (modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            IconButton(modifier = Modifier, onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box (modifier = Modifier.weight(8f), contentAlignment = Alignment.CenterStart) {
            Text(
                modifier = Modifier.offset (y = (-2).dp),
                text = collectionName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        }

        Box (modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
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
            }
        }
    }
}

@Composable
private fun CardList (
    cardList: List<CardViewerModel>,
    onCard: (CardViewerModel) -> Unit
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
                CardComponent(card = card , onCard = onCard)
            }
        }
    }
}

@Composable
private fun CardComponent(
    card: CardViewerModel,
    onCard: (CardViewerModel) -> Unit
) {
    Column (modifier = Modifier.padding(8.dp).fillMaxWidth().clip(MaterialTheme.shapes.large).clickable { onCard(card) }.padding(12.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start) {
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

