package ua.ukma.edu.danki.screens.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.search.viewmodel.SearchEvent
import ua.ukma.edu.danki.screens.search.viewmodel.SearchState

private const val MAX_SEARCH_RESULTS_VISIBILITY = 6

@Composable
internal fun SearchResultsView(
    state: SearchState.SearchResults,
    onEvent: (SearchEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Header(onOpenHistory = { onEvent(SearchEvent.DisplayHistory) })
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                SearchField(
                    input = state.input,
                    onChangeInput = { onEvent(SearchEvent.ChangeInput(it)) },
                    suggestions = state.results,
                    onItemSelected = { onEvent(SearchEvent.SelectWord(it)) }
                )
            }
        }
    }
}

@Composable
private fun Header(
    onOpenHistory: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Dictionary",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        onOpenHistory()
                        menuExpanded = false
                    },
                    text = { Text("History") }
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    input: String,
    onChangeInput: (String) -> Unit,
    suggestions: List<String>,
    onItemSelected: (String) -> Unit
) {
    Box(modifier = Modifier.width(IntrinsicSize.Min), contentAlignment = Alignment.Center) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = input,
            onValueChange = { onChangeInput(it) },
            shape = MaterialTheme.shapes.extraLarge,
            placeholder = { Text(text = "Search...") },
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
        )

        AnimatedVisibility(visible = input.isNotBlank()) {
            val localDensity = LocalDensity.current
            var columnHeight by remember { mutableStateOf(0f) }
            val yOffset = if (columnHeight > 0) {
                (columnHeight * 0.5 + columnHeight / minOf(MAX_SEARCH_RESULTS_VISIBILITY, suggestions.size)).dp
            } else 0.dp
            Column(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = yOffset)
                    .onSizeChanged {
                        with(localDensity) { columnHeight = it.height.toDp().value }
                    },
            ) {
                suggestions.take(MAX_SEARCH_RESULTS_VISIBILITY).forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                onItemSelected(it)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}