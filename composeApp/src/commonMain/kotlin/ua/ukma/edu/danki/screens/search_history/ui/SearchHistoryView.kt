package ua.ukma.edu.danki.screens.search_history.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.search_history.viewmodel.SearchHistoryEvent
import ua.ukma.edu.danki.screens.search_history.viewmodel.SearchHistoryState

@Composable
internal fun SearchHistoryView(
    state: SearchHistoryState.SearchHistoryList,
    onEvent: (SearchHistoryEvent) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Header(onBack = { onEvent(SearchHistoryEvent.GoBack) })
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                SearchedWords(
                    words = state.words,
                    onWordClick = { onEvent(SearchHistoryEvent.SelectWord(it)) }
                )
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
            text = "Search History",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun SearchedWords(
    words: List<String>,
    onWordClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)) {
        items(words) {
            Row(
                modifier = Modifier.padding(8.dp).fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        onWordClick(it)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = it)
            }
        }
    }
}