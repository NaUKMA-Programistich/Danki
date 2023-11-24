package ua.ukma.edu.danki.screens.game.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
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
}