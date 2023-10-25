package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Instant

sealed class CollectionState {
    data class CollectionList(val collections: List<Pair<String, Instant>>) : CollectionState()
    object Loading : CollectionState()
}