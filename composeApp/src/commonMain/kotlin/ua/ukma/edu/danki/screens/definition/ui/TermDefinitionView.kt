package ua.ukma.edu.danki.screens.definition.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionEvent
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionState

@Composable
internal fun TermDefinitionView(
    state: DefinitionState.TermDefinition,
    onEvent: (DefinitionEvent) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Header(onBack = { onEvent(DefinitionEvent.GoBack) })
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)) {
                item { Term(modifier = Modifier.fillMaxWidth(), term = state.term) }
                item { Definition(modifier = Modifier.fillMaxWidth(), definition = state.definition) }
            }
        }
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "Definition",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun Term(
    modifier: Modifier = Modifier,
    term: String
) {
    Column(
        modifier = modifier.padding(8.dp).clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Term", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = term, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun Definition(
    modifier: Modifier = Modifier,
    definition: String
) {
    Column(
        modifier = modifier.padding(8.dp).clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Definition",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = definition,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
