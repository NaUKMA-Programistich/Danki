package ua.ukma.edu.danki.screens.edit_card_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardEvent
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardState

@Composable
fun CardToEditView (
    state: EditCardState.CardToEdit,
    onEvent: (EditCardEvent) -> Unit,
) {
    val onSaveFlow = remember { MutableSharedFlow<Unit>(replay = 1) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            HeaderComponent(
                onCancel = { onEvent(EditCardEvent.Cancel) },
                onDelete = { onEvent(EditCardEvent.DeleteCard(state.card)) },
                onSave = {onSaveFlow.tryEmit(Unit)})
            EditForm(onSaveFlow = onSaveFlow,editCard = state.card, collectionList = state.collectionList, onSave = { card, collectionId -> onEvent(EditCardEvent.SaveCard(card,collectionId)) })
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

            Box (modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                IconButton(modifier = Modifier, onClick = { onCancel() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box (modifier = Modifier.weight(8f), contentAlignment = Alignment.CenterStart) {
                Text(
                    modifier = Modifier.offset (y = (-2).dp),
                    text = "Modify",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start
                )
            }

            Box (modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
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
                        text = { Text("Save") }
                    )
                }
            }
        }
        Divider(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.secondary))
    }
}

@Composable
private fun EditForm(
    editCard: CardDTO,
    onSaveFlow: MutableSharedFlow<Unit>,
    collectionList: List<UserCardCollectionDTO>,
    onSave: (CardDTO,String?) -> Unit,
) {
    var term by remember { mutableStateOf(editCard.term) }
    var definition by remember { mutableStateOf(editCard.definition) }
    var collection = remember { mutableStateOf<UserCardCollectionDTO?>(null) }


    Column(
        modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(16.dp).clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CollectionsList(collection = collection, collectionList = collectionList)

        OutlinedTextField(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            value = term,
            onValueChange = { term = it },
            shape = MaterialTheme.shapes.extraLarge,
            placeholder = { Text(text = "Term") },
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Term")
            },
        )

        OutlinedTextField(
            modifier = Modifier.padding(8.dp).fillMaxWidth().fillMaxHeight(0.6f),
            value = definition,
            onValueChange = { definition = it },
            shape = MaterialTheme.shapes.extraLarge,
            placeholder = { Text(text = "Definition") },
            singleLine = false,
            trailingIcon = {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Definition")
            },
        )
    }


    LaunchedEffect(true) {
        onSaveFlow.collect {
            onSave(
                editCard.copy(
                    term = term,
                    definition = definition
                ), collection.value?.id
            )
        }
    }
}

@Composable
private fun CollectionsList(
    collection: MutableState<UserCardCollectionDTO?>,
    collectionList: List<UserCardCollectionDTO>
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Box (modifier = Modifier.padding(8.dp).fillMaxWidth().fillMaxHeight(0.12f), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.clickable { menuExpanded = true }.align(Alignment.CenterStart),
            text = ("Collections: " + (collection.value?.name?: "")),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )

        DropdownMenu(
            modifier = Modifier.align(Alignment.Center),
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            collectionList.forEach { userCollection ->
                DropdownMenuItem(
                    onClick = {
                        collection.value = userCollection
                        menuExpanded = false
                    },
                    text = { Text(userCollection.name) }
                )
            }
        }
    }


}

