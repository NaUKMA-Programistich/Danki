package ua.ukma.edu.danki.screens.new_cards_viewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.models.UserCardCollectionDTO

@Composable
fun AddAllDialog(
    collections: List<UserCardCollectionDTO>,
    addAll: (UserCardCollectionDTO) -> Unit
) {
    var selectedCollection: UserCardCollectionDTO? by mutableStateOf(null)
    var menuExpanded by remember { mutableStateOf(false) }
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(24.dp),
        ) {

            Box(
                modifier = Modifier.padding(8.dp).fillMaxWidth().fillMaxHeight(0.12f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.clickable { menuExpanded = true }.align(Alignment.CenterStart),
                    text = ("Collection: " + (selectedCollection?.name ?: "")),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                DropdownMenu(
                    modifier = Modifier.align(Alignment.Center),
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    collections.forEach { userCollection ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCollection = userCollection
                                menuExpanded = false
                            },
                            text = { Text(userCollection.name) }
                        )
                    }
                }
            }

            TextButton(onClick = {
                selectedCollection?.let {
                    addAll(it)
                }
            }) {
                Text(
                    text = "Add all",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

    }
}