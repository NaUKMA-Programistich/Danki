package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent

@Composable
fun CreateCollectionDialog(
    collectionName: String = "",
    onCloseClick: () -> Unit,
    onEvent: (CollectionEvent) -> Unit
) {
    var name by remember { mutableStateOf(collectionName) }
    Surface {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Text("New collection", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("name") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onCloseClick) {
                    Text("Cancel")
                }
                TextButton(onClick = {
                    onEvent(CollectionEvent.SaveCollection(name))
                    onCloseClick()
                }) {
                    Text("Save")
                }
            }
        }

    }
}