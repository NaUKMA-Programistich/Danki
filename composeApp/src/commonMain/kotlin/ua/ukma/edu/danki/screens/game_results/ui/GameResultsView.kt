package ua.ukma.edu.danki.screens.game_results.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsEvent
import ua.ukma.edu.danki.screens.game_results.viewmodel.GameResultsState
import ua.ukma.edu.danki.theme.surfaceContainer


@Composable
internal fun GameResultsView(
    state: GameResultsState.ShowGameResults,
    onEvent: (GameResultsEvent) -> Unit
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            GameResultsViewSmall(state, onEvent)
        } else {
            GameResultsViewLarge(state, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Header(onCloseResults: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = "Play", style = MaterialTheme.typography.titleLarge) },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onCloseResults()
                            menuExpanded = false
                        },
                        text = { Text("Close results") }
                    )
                }
            }
        }
    )
}

@Composable
internal fun ResultAsItem(
    cardAndSuccess: Pair<CardDTO, Boolean>,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    modifier = Modifier.fillMaxHeight(),
                    text = cardAndSuccess.first.term,
                    style = MaterialTheme.typography.labelLarge
                )
                if (cardAndSuccess.second)
                    Icon(Icons.Default.Check, "success icon")
                else
                    Icon(
                        Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "error icon"
                    )
            }
            AnimatedVisibility(
                visible = expanded,
            ) {
                Text(cardAndSuccess.first.definition, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}