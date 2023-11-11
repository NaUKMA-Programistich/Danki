package ua.ukma.edu.danki.screens.game.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.game.viewmodel.GameEvent
import ua.ukma.edu.danki.screens.game.viewmodel.GameState
import ua.ukma.edu.danki.theme.surfaceContainerLow


@Composable
internal fun GameView(
    state: GameState.GameInProgress,
    onEvent: (GameEvent) -> Unit
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            GameViewSmall(state, onEvent)
        } else {
            GameViewLarge(state, onEvent)
        }
    }
}

@Composable
internal fun GameComponent(
    state: GameState.GameInProgress,
    onEvent: (GameEvent) -> Unit,
    cardTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${state.score} / ${state.gameCards.size}", style = MaterialTheme.typography.titleMedium)
            CardSurface(state.currentCard, cardTextStyle)
        }
        Column(
            Modifier.weight(2f),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowDefinitionButton(onClick = { onEvent(GameEvent.ShowDefinition) })
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.width(193.dp)) {
                FailureButton(onClick = { onEvent(GameEvent.NextCard(false)) })
                SuccessButton(onClick = { onEvent(GameEvent.NextCard(true)) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Header() {
    TopAppBar(
        title = { Text(text = "Play", style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
            }
        }
    )
}

@Composable
internal fun CardSurface(card: String, textStyle: TextStyle) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = card, textAlign = TextAlign.Center,
                style = textStyle
            )
        }
    }
}

@Composable
internal fun ShowDefinitionButton(onClick: () -> Unit) {
    ElevatedButton(onClick = onClick) { Text("Show") }
}

@Composable
internal fun SuccessButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) { Icon(Icons.Default.Check, "correct icon") }
}

@Composable
internal fun FailureButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.errorContainer
    ) { Icon(Icons.Default.Close, "incorrect icon") }
}