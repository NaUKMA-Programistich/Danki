package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState


@Composable
internal fun CollectionViewList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var currentSortParam by remember { mutableStateOf(CollectionSortParam.ByName) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button(onClick = {}) { Text("Favorite") }
            Button(onClick = { menuExpanded = true }) {
                Text(currentSortParam.paramToString())
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                enumValues<CollectionSortParam>().forEach {
                    DropdownMenuItem(
                        onClick = {
                            onEvent(CollectionEvent.SortList(it))
                            currentSortParam = it
                            menuExpanded = false
                        },
                        text = { Text(it.paramToString()) },
                        enabled = currentSortParam != it
                    )
                }
            }
        }
        LazyColumn {
            items(state.collections) { collection ->
                Column {
                    Text(collection.lastModified.toString())
                    Text(collection.name)
                }
            }

        }
    }
}

fun CollectionSortParam.paramToString(): String {
    if (this == CollectionSortParam.ByName)
        return "By name"
    return "By date"
}