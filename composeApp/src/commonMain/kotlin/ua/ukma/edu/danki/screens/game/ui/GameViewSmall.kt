package ua.ukma.edu.danki.screens.game.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.game.viewmodel.GameEvent
import ua.ukma.edu.danki.screens.game.viewmodel.GameState

@Composable
internal fun GameViewSmall(
    state: GameState.GameInProgress,
    onEvent: (GameEvent) -> Unit
) {
    Scaffold(
        topBar = { Header() },
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            GameComponent(
                state,
                onEvent,
                MaterialTheme.typography.titleLarge,
                Modifier.padding(innerPadding).padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
