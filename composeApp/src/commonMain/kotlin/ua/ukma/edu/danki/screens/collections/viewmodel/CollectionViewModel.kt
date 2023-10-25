package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel

class CollectionViewModel :
    ViewModel<CollectionState, CollectionAction, CollectionEvent>(initialState = CollectionState.Loading) {
    init {
        withViewModelScope {
            getCollections()
        }
    }

    override fun obtainEvent(viewEvent: CollectionEvent) {
        when (viewEvent) {
            is CollectionEvent.SortList -> sort(viewEvent.sortingMethod)
        }
    }

    private fun getCollections() {
        withViewModelScope {
            setViewState(CollectionState.CollectionList(listOf(Pair("first", Clock.System.now()))))
        }
    }

    private fun sort(sortingMethod: SortingMethod) {
        //TODO("sort collections")
    }
}