package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CollectionState {
    data class CollectionList(val collections: List<UserCardCollectionDTO>) : CollectionState()
    object Loading : CollectionState()
}