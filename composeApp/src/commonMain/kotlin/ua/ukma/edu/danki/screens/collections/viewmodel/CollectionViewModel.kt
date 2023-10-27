package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import kotlin.time.Duration.Companion.hours

class CollectionViewModel :
    ViewModel<CollectionState, CollectionAction, CollectionEvent>(initialState = CollectionState.Loading) {
    init {
        withViewModelScope {
            getCollections()
        }
    }

    override fun obtainEvent(viewEvent: CollectionEvent) {
        when (viewEvent) {
            is CollectionEvent.SortList -> sort(viewEvent.sortParam)
        }
    }


    private fun getCollections() {
        withViewModelScope {
            val mockData = listOf(
                UserCardCollectionDTO(
                    "UniqueID1", "first", Clock.System.now().minus(10.hours),
                    own = true, true
                ),
                UserCardCollectionDTO(
                    "UniqueID2", "second", Clock.System.now().minus(20.hours),
                    own = true, false
                ),
                UserCardCollectionDTO(
                    "UniqueID2", "third", Clock.System.now().minus(30.hours),
                    own = true, false
                ),
                UserCardCollectionDTO(
                    "UniqueID3", "forth", Clock.System.now().minus(1.hours),
                    own = false, true
                ),
            )

            //TODO("collecting real data from db/server")
            setViewState(
                CollectionState.CollectionList(mockData)
            )
        }
    }

    private fun sort(sortParam: CollectionSortParam) {
        //TODO("sort collections")
    }
}