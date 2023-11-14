package ua.ukma.edu.danki.screens.new_cards_viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import ua.ukma.edu.danki.screens.new_cards_viewer.model.NewCardViewerModel
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerEvent
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerState

@Composable
fun NewCardViewerView (
    state: NewCardViewerState.NewCardCards,
    onEvent: (NewCardViewerEvent) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            HeaderComponent(onBack = { onEvent(NewCardViewerEvent.GoBack) })
            CardList(newCardViewerModelMap = state.newCardViewerModelMap , onCard = { onEvent(NewCardViewerEvent.OnCardClick(newCardViewerModel = it)) })
        }
    }
}

@Composable
private fun HeaderComponent (onBack: () -> Unit) {
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
                text = "New Cards",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun CardList (
    newCardViewerModelMap: Map<UserCardCollectionDTO?,MutableList<NewCardViewerModel>>,
    onCard: (NewCardViewerModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(16.dp).clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        newCardViewerModelMap.forEach { pair ->

            item {
                Text(
                    modifier = Modifier,
                    text = pair.key?.name?.toString() ?: "...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                pair.value.forEach {
                    CardComponent(card = it , onCard = onCard)
                }
            }
        }
    }
}

@Composable
private fun CardComponent(
    card: NewCardViewerModel,
    onCard: (NewCardViewerModel) -> Unit
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

