package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState


@Composable
internal fun CollectionViewList(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            CollectionViewSmall(state, onEvent)
        } else {
            CollectionViewLarge(state, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Header(
    onEvent: (CollectionEvent) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = "Collections", style = MaterialTheme.typography.titleLarge) },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onEvent(CollectionEvent.ShowCreateCollectionDialog)
                            menuExpanded = false
                        },
                        text = { Text("Create collection") }
                    )
                }
            }
        }
    )
}

@Composable
internal fun FavoriteAndSortButtonsRow(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        FavoriteButton(favoriteOnlyIsOn = state.favoriteOnly, onClick = {
            if (!state.favoriteOnly) onEvent(CollectionEvent.ShowOnlyFavorites)
            else onEvent(CollectionEvent.ShowAll)
        })
        SortMenu(state, onEvent)
        OrderButton(state.orderIsAscending, onClick = { onEvent(CollectionEvent.ChangeSortingOrder) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoriteButton(
    favoriteOnlyIsOn: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        label = { Text("Favorite") },
        elevation = null,
        selected = favoriteOnlyIsOn,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SortMenu(
    state: CollectionState.CollectionList,
    onEvent: (CollectionEvent) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        ElevatedFilterChip(
            onClick = { menuExpanded = true },
            shape = MaterialTheme.shapes.medium,
            label = { Text(state.sortingParam.paramToString(), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = false,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "arrow down icon") },
        )
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            enumValues<CollectionSortParam>().forEach { sortParam ->
                DropdownMenuItem(
                    onClick = {
                        onEvent(CollectionEvent.SortList(sortParam))
                        menuExpanded = false
                    },
                    text = { Text(sortParam.paramToString()) },
                    enabled = state.sortingParam != sortParam,
                )
            }
        }
    }
}

@Composable
internal fun OrderButton(
    isAscending: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(checked = isAscending, onCheckedChange = { onClick() }) {
        Icon(
            if (isAscending) Icons.Default.KeyboardArrowUp
            else Icons.Default.KeyboardArrowDown,
            "ascending order is $isAscending"
        )
    }
}

@Composable
internal fun CollectionAsItem(
    collection: UserCardCollectionDTO,
    onEvent: (CollectionEvent) -> Unit
) {
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
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = { onEvent(CollectionEvent.ChangeFavoriteStatus(collection.id)) }) {
            //TODO("Icons.Outlined.Star is not actually outlined,
            // so now we use Favorite/FavoriteBorder")
            Icon(
                if (collection.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite button",
                tint = MaterialTheme.colorScheme.secondary
            )
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