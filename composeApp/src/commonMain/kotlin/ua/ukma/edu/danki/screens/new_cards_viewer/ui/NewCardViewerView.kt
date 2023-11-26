package ua.ukma.edu.danki.screens.new_cards_viewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerEvent
import ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel.NewCardViewerState

@Composable
fun NewCardViewerView(
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
            CardList(newCards = state.newCards, onCard = { onEvent(NewCardViewerEvent.OnCardClick(newCard = it)) })
        }
    }
}

@Composable
private fun HeaderComponent(onBack: () -> Unit) {
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
                text = "New Cards",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun CardList(
    newCards: List<CardDTO>,
    onCard: (CardDTO) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .sizeIn(
                maxWidth = 612.dp
            )
            .fillMaxWidth()
            .fillMaxHeight().padding(16.dp).clip(MaterialTheme.shapes.large),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        newCards.forEach { card ->
            item {
                Text(
                    modifier = Modifier,
                    text = "...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
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

