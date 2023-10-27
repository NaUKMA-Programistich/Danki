package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.CollectionSortParam

sealed class CollectionEvent {
    data class SortList(val sortParam: CollectionSortParam) : CollectionEvent()
}