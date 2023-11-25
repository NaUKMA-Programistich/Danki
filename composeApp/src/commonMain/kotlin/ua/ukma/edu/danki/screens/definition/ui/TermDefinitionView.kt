package ua.ukma.edu.danki.screens.definition.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionEvent
import ua.ukma.edu.danki.screens.definition.viewmodel.DefinitionState

@Composable
internal fun TermDefinitionView(
    state: DefinitionState.TermDefinition,
    onEvent: (DefinitionEvent) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            HeaderComponent(onBack = { onEvent(DefinitionEvent.GoBack) }, onNewCard = {
                onEvent(
                    DefinitionEvent.OnNewCardClick(
                        CardDTO(
                            term = state.term,
                            definition = state.definitions[state.selectedDefinitionIndex].definition
                        )
                    )
                )
            })
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)) {
                item { Term(modifier = Modifier.fillMaxWidth(), term = state.term) }
                itemsIndexed(state.definitions) { index, definition ->
                    Definition(
                        modifier = Modifier.fillMaxWidth(),
                        definition = definition.definition,
                        wordType = definition.wordType.name,
                        onClick = { onEvent(DefinitionEvent.SelecteDefinition(index)) },
                        isSelected = index == state.selectedDefinitionIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderComponent(onBack: () -> Unit, onNewCard: () -> Unit = {}) {
    var menuExpanded by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = { onBack() }) {
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
                    text = "Definition",
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
                            onNewCard()
                            menuExpanded = false
                        },
                        text = { Text("Create") }
                    )
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.secondary))
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
    definition: String,
    wordType: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Column(
        modifier = modifier.padding(8.dp).clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
            .then(
                if (isSelected) Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large
                ) else Modifier
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Definition ($wordType)",
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
