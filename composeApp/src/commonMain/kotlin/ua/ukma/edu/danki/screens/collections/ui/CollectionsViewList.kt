package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Header() {
    TopAppBar(
        title = { Text(text = "Collections", style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteButton(
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
fun SortMenu(
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
            trailingIcon = {Icon(Icons.Default.ArrowDropDown, "arrow down icon")},
        )

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

@Composable
fun CollectionAsItem(
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
            //TODO("Icons.Outlined.Star is not actually outlined
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