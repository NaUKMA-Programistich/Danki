package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CollectionState {
    data class CollectionList(
        val collections: List<UserCardCollectionDTO>,
        val sortingParam: CollectionSortParam,
        val orderIsAscending: Boolean,
        val favoriteOnly: Boolean,
    ) : CollectionState()
    data object Loading : CollectionState()
}