package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.CollectionSortParam

sealed class CollectionEvent {
    data object ShowAll: CollectionEvent()
    data class SortList(val sortParam: CollectionSortParam) : CollectionEvent()
    data object ChangeSortingOrder : CollectionEvent()
    data object ShowOnlyFavorites : CollectionEvent()
    data class ChangeFavoriteStatus(val id: String): CollectionEvent()
}