package ua.ukma.edu.danki.screens.edit_card_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardEvent
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardState

@Composable
fun CardToEditView (
    state: EditCardState.CardToEdit,
    onEvent: (EditCardEvent) -> Unit,
) {
    val onSaveFlow = remember { MutableSharedFlow<Unit>(replay = 1) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            HeaderComponent(
                onCancel = { onEvent(EditCardEvent.Cancel) },
                onDelete = { onEvent(EditCardEvent.DeleteCard(state.editCard)) },
                onSave = {onSaveFlow.tryEmit(Unit)})
            EditForm(onSaveFlow = onSaveFlow,editCard = state.editCard, collectionList = state.collectionList, onSave = { onEvent(EditCardEvent.SaveCard(it)) })
        }
    }
}

@Composable
private fun HeaderComponent (onCancel: () -> Unit, onDelete : () -> Unit = {}, onSave : () -> Unit = {}) {
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
            IconButton(modifier = Modifier.weight(1f), onClick = { onCancel() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier.weight(7f),
                text = "Modify",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
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
                            onDelete()
                            menuExpanded = false
                        },
                        text = { Text("Delete") }
                    )

                    DropdownMenuItem(
                        onClick = {
                            onSave()
                            menuExpanded = false
                        },
                        text = { Text("Dave") }
                    )
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
    }
}

@Composable
private fun EditForm(
    editCard: EditCard,
    onSaveFlow: MutableSharedFlow<Unit>,
    collectionList: List<UserCardCollectionDTO>,
    onSave: (EditCard) -> Unit,
) {
    var term by remember { mutableStateOf(editCard.term) }
    var definition by remember { mutableStateOf(editCard.definition) }
    var collection by remember { mutableStateOf<UserCardCollectionDTO?>(null) }

    var menuExpanded by remember { mutableStateOf(false) }


    Box(
        Modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(8.dp).clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = term,
                onValueChange = { term = it },
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = { Text(text = "Term") },
                singleLine = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Term")
                },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = definition,
                onValueChange = { definition = it },
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = { Text(text = "Definition") },
                singleLine = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Definition")
                },
            )

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                collectionList.forEach { userCollection ->
                    DropdownMenuItem(
                        onClick = {
                            collection = userCollection
                            menuExpanded = false
                        },
                        text = { Text(userCollection.name) }
                    )
                }
            }
        }
    }


    LaunchedEffect(true) {
        onSaveFlow.collect {
            onSave(editCard.copy(
                term = term,
                definition = definition,
                collection = collection
            ))
        }
    }
}

