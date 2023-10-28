package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState


@Composable
internal fun CollectionViewList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FavoriteButton(favoriteOnlyIsOn = state.favoriteOnly, onClick = {
                    if (!state.favoriteOnly)
                        onEvent(CollectionEvent.ShowOnlyFavorites)
                    else
                        onEvent(CollectionEvent.ShowAll)
                })
                SortMenu(state, onEvent)
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.collections) { collection ->
                    Column() {
                        Text(
                            collection.lastModified.timeToString(),
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                collection.name,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = { onEvent(CollectionEvent.ChangeFavoriteStatus(collection.id)) }) {
                                //TODO("Icons.Outlined.Star is not actually outlined
                                // so now we use Favorite/FavoriteBorder")
                                Icon(
                                    if (collection.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite button",
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun FavoriteButton(
    favoriteOnlyIsOn: Boolean,
    onClick: () -> Unit
) {
    if (favoriteOnlyIsOn)
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text("Favorite")
        }
    else
        OutlinedButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
        ) {
            Text("Favorite", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
}

@Composable
fun SortMenu(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        ElevatedButton(
            onClick = { menuExpanded = true },
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(state.sortingParam.paramToString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            enumValues<CollectionSortParam>().forEach {
                DropdownMenuItem(
                    onClick = {
                        onEvent(CollectionEvent.SortList(it))
                        menuExpanded = false
                    },
                    text = { Text(it.paramToString()) },
                    enabled = state.sortingParam != it,

                    )
            }

        }
    }
}

fun CollectionSortParam.paramToString(): String {
    if (this == CollectionSortParam.ByName)
        return "By name"
    return "By date"
}

fun Instant.timeToString(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.UTC)
    return "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}/${localDateTime.year}"
}