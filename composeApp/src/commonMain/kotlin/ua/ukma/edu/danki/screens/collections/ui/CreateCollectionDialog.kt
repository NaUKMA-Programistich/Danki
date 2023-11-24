package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent

@Composable
fun CreateCollectionDialog(
    collection: UserCardCollectionDTO? = null,
    onCloseClick: () -> Unit,
    onEvent: (CollectionEvent) -> Unit
) {
    var name by remember { mutableStateOf(collection?.name ?: "") }
    Surface {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Text(
                if (collection == null) "New collection" else "Change collection name",
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
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
                    if (collection == null)
                        onEvent(CollectionEvent.CreateCollection(name))
                    else
                        onEvent(
                            CollectionEvent.UpdateCollection(
                                UserCardCollectionDTO(
                                    collection.id, name, collection.lastModified, collection.own, collection.favorite
                                )
                            )
                        )
                    onCloseClick()
                }) {
                    Text("Save")
                }
            }
        }

    }
}