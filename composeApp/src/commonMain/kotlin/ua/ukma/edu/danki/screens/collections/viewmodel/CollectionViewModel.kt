package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import kotlin.time.Duration.Companion.hours

class CollectionViewModel :
    ViewModel<CollectionState, CollectionAction, CollectionEvent>(initialState = CollectionState.Loading) {
    private val mockData: List<UserCardCollectionDTO> = listOf(
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
            "UniqueID7", "any", Clock.System.now().minus(1.hours),
            own = false, true
        ),
        UserCardCollectionDTO(
            "UniqueID8", "eights", Clock.System.now().minus(1.hours),
            own = false, true
        ),
    )

    init {
        withViewModelScope {
            getCollections()
        }
    }

    override fun obtainEvent(viewEvent: CollectionEvent) {
        when (viewEvent) {
            is CollectionEvent.ShowAll -> getCollections()
            is CollectionEvent.SortList -> sort(viewEvent.sortParam)
            is CollectionEvent.ChangeSortingOrder -> changeOrder()
            is CollectionEvent.ShowOnlyFavorites -> showOnlyFavorites()
            is CollectionEvent.ChangeFavoriteStatus -> changeCollectionFavoriteStatus(viewEvent.id)
            is CollectionEvent.OpenCollection -> processOpenCollection(viewEvent.collection)
        }
    }

    private fun getCollections() {
        withViewModelScope {
            //TODO("collecting real data from db/server")

            setViewState(
                CollectionState.CollectionList(
                    mockData.sortedBy { it.name.lowercase() },
                    CollectionSortParam.ByName,
                    orderIsAscending = true,
                    favoriteOnly = false
                )
            )
        }
    }

    private fun showOnlyFavorites() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            setViewState(
                CollectionState.CollectionList(
                    state.collections.filter { it.favorite },
                    state.sortingParam,
                    state.orderIsAscending,
                    true
                )
            )
        }
    }

    private fun sort(sortParam: CollectionSortParam) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            if (sortParam == CollectionSortParam.ByName)
                setViewState(
                    CollectionState.CollectionList(
                        state.collections.sortedBy { it.name.lowercase() },
                        sortParam,
                        orderIsAscending = true,
                        state.favoriteOnly
                    )
                )
            else
                setViewState(
                    CollectionState.CollectionList(
                        state.collections.sortedBy { it.lastModified },
                        sortParam,
                        orderIsAscending = true,
                        state.favoriteOnly
                    )
                )
        }
    }

    private fun changeOrder() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            setViewState(
                CollectionState.CollectionList(
                    state.collections.reversed(),
                    state.sortingParam,
                    !state.orderIsAscending,
                    state.favoriteOnly
                )
            )
        }
    }

    private fun changeCollectionFavoriteStatus(id: String) {
        withViewModelScope {
            //TODO("change collection favorite status in server/local db")
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            // should be retrieved from db and not changed with .map() here
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
                    },
                    state.sortingParam, state.orderIsAscending, state.favoriteOnly
                )
            )
        }
    }

    private fun processOpenCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            setViewAction(CollectionAction.OpenCollection(collection))
        }
    }
}
