package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.screens.collections.viewmodel.SortingMethod

@Composable
internal fun CollectionViewList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var dropDownMenuCurrent by remember { mutableStateOf(SortingMethod.ByName) }

    Column {
        Row {
            Button(onClick = {}) { Text("Shared") }
            Button(onClick = { dropDownMenuExpanded = true }) {
                Text(dropDownMenuCurrent.toString())
            }
            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        if (dropDownMenuCurrent != SortingMethod.ByName) {
                            onEvent(CollectionEvent.SortList(SortingMethod.ByName))
                            dropDownMenuCurrent = SortingMethod.ByName
                        }
                        dropDownMenuExpanded = false
                    },
                    text = { Text("By name") }
                )
                DropdownMenuItem(
                    onClick = {
                        if (dropDownMenuCurrent != SortingMethod.ByDate) {
                            onEvent(CollectionEvent.SortList(SortingMethod.ByDate))
                            dropDownMenuCurrent = SortingMethod.ByDate
                        }
                        dropDownMenuExpanded = false
                    },
                    text = { Text("By date") }
                )
            }
        }
        LazyColumn {
            items(state.collections) { collection ->
                Column {
                    Text(collection.first)
                    Text(collection.second.toString())
                }
            }
        }
    }
}