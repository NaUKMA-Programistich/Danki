package ua.ukma.edu.danki.screens.game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.game.viewmodel.GameEvent
import ua.ukma.edu.danki.screens.game.viewmodel.GameState

@Composable
internal fun GameViewLarge(
    state: GameState.GameInProgress,
    onEvent: (GameEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(onFinishGame = { onEvent(GameEvent.FinishGame) })
            Row {
                SideNavigation()

                GameComponent(
                    state,
                    onEvent,
                    MaterialTheme.typography.titleLarge,
                    Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
                )
            }
        }
    }
}

//TODO move side navigation to core/composable?
@Composable
private fun SideNavigation() {
    NavigationRail(modifier = Modifier.padding(top = 44.dp, bottom = 56.dp)) {
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