package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import kotlin.time.Duration.Companion.hours

class CollectionViewModel :
    ViewModel<CollectionState, CollectionAction, CollectionEvent>(initialState = CollectionState.Loading) {
    private val mockData: List<UserCardCollectionDTO>

    init {
        mockData = listOf(
            UserCardCollectionDTO(
                "UniqueID1", "First", Clock.System.now().minus(10.hours),
                own = true, true
            ),
            UserCardCollectionDTO(
                "UniqueID2", "Second", Clock.System.now().minus(40.hours),
                own = true, false
            ),
            UserCardCollectionDTO(
                "UniqueID3", "third", Clock.System.now().minus(100.hours),
                own = true, false
            ),
            UserCardCollectionDTO(
                "UniqueID4", "forth", Clock.System.now().minus(1.hours),
                own = false, true
            ),
            UserCardCollectionDTO(
                "UniqueID5", "fifth", Clock.System.now().minus(1.hours),
                own = false, true
            ),
            UserCardCollectionDTO(
                "UniqueID6", "sixth", Clock.System.now().minus(1.hours),
                own = false, true
            ),
            UserCardCollectionDTO(
                "UniqueID7", "seventh", Clock.System.now().minus(1.hours),
                own = false, true
            ),
            UserCardCollectionDTO(
                "UniqueID8", "eights", Clock.System.now().minus(1.hours),
                own = false, true
            ),
        )

        withViewModelScope {
            getCollections()
        }
    }

    override fun obtainEvent(viewEvent: CollectionEvent) {
        when (viewEvent) {
            is CollectionEvent.ShowAll -> getCollections()
            is CollectionEvent.SortList -> sort(viewEvent.sortParam)
            is CollectionEvent.ShowOnlyFavorites -> showOnlyFavorites()
            is CollectionEvent.ChangeFavoriteStatus -> changeCollectionFavoriteStatus(viewEvent.id)

        }
    }

    private fun getCollections() {
        withViewModelScope {
            //TODO("collecting real data from db/server")

            setViewState(
                CollectionState.CollectionList(mockData, CollectionSortParam.ByName, false)
            )
        }
    }

    private fun showOnlyFavorites() {
        withViewModelScope {
            //TODO("This implementation works, but maybe we should get only favorites from server/localDB?")

            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            setViewState(
                CollectionState.CollectionList(
                    state.collections.filter { it.favorite },
                    state.sortingParam,
                    true
                )
            )
        }
    }

    private fun sort(sortParam: CollectionSortParam) {
        withViewModelScope {
            //TODO("This implementation works, but maybe we should get sorted collections from server/localDB?")

            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            if (sortParam == CollectionSortParam.ByName)
                setViewState(
                    CollectionState.CollectionList(
                        state.collections.sortedBy { it.name.lowercase() }, sortParam, state.favoriteOnly
                    )
                )
            else
                setViewState(
                    CollectionState.CollectionList(
                        state.collections.sortedBy { it.lastModified }, sortParam, state.favoriteOnly
                    )
                )
        }
    }

    private fun changeCollectionFavoriteStatus(id: String) {
        withViewModelScope {
            //TODO("change collection favorite status in server/local db")
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            setViewState(
                CollectionState.CollectionList(
                    state.collections.map {
                        if (it.id == id)
                            UserCardCollectionDTO(
                                it.id,
                                it.name,
                                Clock.System.now(),
                                it.own,
                                !it.favorite
                            )
                        else
                            it
                    }, state.sortingParam, state.favoriteOnly
                )
            )
        }
    }
}
