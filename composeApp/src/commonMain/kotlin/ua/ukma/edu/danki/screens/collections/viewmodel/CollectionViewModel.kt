package ua.ukma.edu.danki.screens.collections.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.GetUserCollections
import ua.ukma.edu.danki.models.UpdateCollectionRequest
import ua.ukma.edu.danki.models.UserCardCollectionDTO

class CollectionViewModel(
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository
) : ViewModel<CollectionState, CollectionAction, CollectionEvent>(initialState = CollectionState.Loading) {
//    private val mockData: List<UserCardCollectionDTO> = listOf(
//        UserCardCollectionDTO(
//            "UniqueID1", "First", Clock.System.now().minus(10.hours),
//            own = true, true
//        ),
//        UserCardCollectionDTO(
//            "UniqueID2", "Second", Clock.System.now().minus(40.hours),
//            own = true, false
//        ),
//        UserCardCollectionDTO(
//            "UniqueID3", "third", Clock.System.now().minus(100.hours),
//            own = true, false
//        ),
//        UserCardCollectionDTO(
//            "UniqueID4", "forth", Clock.System.now().minus(1.hours),
//            own = false, true
//        ),
//        UserCardCollectionDTO(
//            "UniqueID5", "fifth", Clock.System.now().minus(1.hours),
//            own = false, true
//        ),
//        UserCardCollectionDTO(
//            "UniqueID6", "sixth", Clock.System.now().minus(1.hours),
//            own = false, true
//        ),
//        UserCardCollectionDTO(
//            "UniqueID7", "any", Clock.System.now().minus(1.hours),
//            own = false, true
//        ),
//        UserCardCollectionDTO(
//            "UniqueID8", "eights", Clock.System.now().minus(1.hours),
//            own = false, true
//        ),
//    )

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
            is CollectionEvent.CreateCollection -> processCreateCollection(viewEvent.collection)
            is CollectionEvent.UpdateCollection -> processUpdateCollection(viewEvent.collection)
            is CollectionEvent.ChangeCollectionName -> processChangeCollectionName(viewEvent.collection)
            is CollectionEvent.DeleteCollection -> processDeleteCollection(viewEvent.collectionId)
            is CollectionEvent.DeleteSelected -> processDeleteSelected()
            is CollectionEvent.ShowCreateCollectionDialog -> processShowCreateCollectionDialog()
            is CollectionEvent.ToggleSelectCollection -> processToggleSelectCollection(viewEvent.collectionId)
        }
    }

    private fun getCollections() {
        withViewModelScope {
            val listOfUserCollections = cardCollectionsRepository.getUserCollections(GetUserCollections())
            val collections = listOfUserCollections?.cardCollections ?: emptyList()

            setViewState(
                CollectionState.CollectionList(
                    collections.sortedBy { it.name.lowercase() },
                    HashSet(),
                    selectionMode = false,
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
                    state.selected,
                    state.selectionMode,
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
                        state.selected,
                        state.selectionMode,
                        sortParam,
                        orderIsAscending = true,
                        state.favoriteOnly
                    )
                )
            else
                setViewState(
                    CollectionState.CollectionList(
                        state.collections.sortedBy { it.lastModified },
                        state.selected,
                        state.selectionMode,
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
                    state.selected,
                    state.selectionMode,
                    state.sortingParam,
                    !state.orderIsAscending,
                    state.favoriteOnly
                )
            )
        }
    }

    private fun changeCollectionFavoriteStatus(id: String) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            val initialFavoriteStatus = state.collections.find { it.id == id }?.favorite ?: false
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
                    state.selected, state.selectionMode,
                    state.sortingParam, state.orderIsAscending, state.favoriteOnly
                )
            )

            cardCollectionsRepository.updateCollection(
                UpdateCollectionRequest(
                    uuid = id,
                    favorite = !initialFavoriteStatus
                )
            )
        }
    }

    private fun processOpenCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            setViewAction(CollectionAction.OpenCollection(collection))
        }
    }

    private fun processCreateCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            // TODO create real collection

            mockData.add(
                UserCardCollectionDTO("", collection.name, Clock.System.now(), own = true, favorite = false)
            )
            getCollections()
        }
    }

    private fun processUpdateCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            //TODO update collection

            mockData.removeAll { it.id == collection.id }
            mockData.add(collection)
            getCollections()
        }
    }

    private fun processChangeCollectionName(collection: UserCardCollectionDTO) {
        withViewModelScope {
            setViewAction(CollectionAction.ShowChangeCollectionNameDialog(collection))
        }
    }

    private fun processDeleteCollection(collectionId: String) {
        withViewModelScope {
            //TODO real delete collections
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            mockData.removeAll { it.id == collectionId }
            getCollections()
        }
    }

    private fun processDeleteSelected() {
        withViewModelScope {
            //TODO real delete collections
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            mockData.removeAll { state.selected.contains(it.id) }
            getCollections()
        }
    }

    private fun processShowCreateCollectionDialog() {
        withViewModelScope {
            setViewAction(CollectionAction.ShowCreateCollectionDialog)
        }
    }

    private fun processToggleSelectCollection(collectionId: String) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CollectionState.CollectionList) return@withViewModelScope

            if (state.selected.contains(collectionId)) {
                state.selected.remove(collectionId)
                setViewState(
                    CollectionState.CollectionList(
                        state.collections,
                        if (!state.selected.isEmpty()) state.selected else HashSet(),
                        !state.selected.isEmpty(),
                        state.sortingParam,
                        state.orderIsAscending,
                        state.favoriteOnly
                    )
                )
                return@withViewModelScope
            }

            state.selected.add(collectionId)
            setViewState(
                CollectionState.CollectionList(
                    state.collections,
                    state.selected,
                    selectionMode = true,
                    state.sortingParam,
                    state.orderIsAscending,
                    state.favoriteOnly
                )
            )
        }
    }
}
