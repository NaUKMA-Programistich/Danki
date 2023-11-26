package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState


class CardCollectionViewerViewModel(
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository,
    private val cardRepository: CardRepository = Injection.cardRepository,
    private val collection: UserCardCollectionDTO
) : ViewModel<CardCollectionViewerState, CardCollectionViewerAction, CardCollectionViewerEvent>(initialState = CardCollectionViewerState.Loading) {



    override fun obtainEvent(viewEvent: CardCollectionViewerEvent) {
        when (viewEvent) {
            is CardCollectionViewerEvent.OnCardClick -> processOnCardClick(viewEvent.card)
            is CardCollectionViewerEvent.DeleteCollection -> processDeletingCollection(collection = viewEvent.collection)
            is CardCollectionViewerEvent.SortList ->  processSorting(sortingParam = viewEvent.sortParam , orderIsAscending = null)
            is CardCollectionViewerEvent.ChangeSortingOrder ->  processSorting(sortingParam = null , orderIsAscending = viewEvent.orderIsAscending)
            CardCollectionViewerEvent.GoBack -> processGoBack()
            CardCollectionViewerEvent.ShareCollection -> processShareCollection()
            CardCollectionViewerEvent.PlayGame -> processPlayGame()
        }
    }

    init {
        withViewModelScope {
            setViewState(CardCollectionViewerState.Loading)
            setViewState(
                CardCollectionViewerState.CollectionCards(
                    collection = collection,
                    cardList = getCardViewerModelList()
                )
            )
        }
    }

    private fun processSorting (sortingParam : CardSortParam?,orderIsAscending : Boolean?) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CardCollectionViewerState.CollectionCards) return@withViewModelScope
            val processSortParam = sortingParam ?: state.sortingParam
            val processOrderIsAscending = orderIsAscending ?: state.orderIsAscending
            val cards = state.cardList
            val sortedList = when (processSortParam) {
                CardSortParam.ByTerm -> {
                    if (processOrderIsAscending) {
                        cards.sortedBy { it.term }
                    } else {
                        cards.sortedByDescending { it.term }
                    }
                }
                CardSortParam.ByTimeAdded -> {
                    if (processOrderIsAscending) {
                        cards.sortedBy { it.timeAdded }
                    } else {
                        cards.sortedByDescending { it.timeAdded }
                    }
                }
                CardSortParam.ByLastModified -> {
                    if (processOrderIsAscending) {
                        cards.sortedBy { it.lastModified }
                    } else {
                        cards.sortedByDescending { it.lastModified }
                    }
                }
            }
            setViewState(
                state.copy(
                    orderIsAscending = processOrderIsAscending,
                    sortingParam = processSortParam,
                    cardList = sortedList
                )
            )
        }
    }

    private suspend fun getCardViewerModelList(): List<CardDTO> {
        val listOfCards = cardRepository.getCardsOfCollection(
            GetCardsOfCollection(
                collection = collection.id
            )
        )
        return listOfCards?.cards ?: emptyList()
    }

    private fun processDeletingCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            cardCollectionsRepository.deleteCollection(
                DeleteCollectionsRequest(collections = listOf(collection.id))
            )
            setViewAction(CardCollectionViewerAction.NavigateBack)
        }
    }

    private fun processShareCollection() {
        withViewModelScope {
            val code = cardCollectionsRepository.shareCollection(
                ShareCollectionRequest(uuid = collection.id)
            )?.id
            if (code != null) {
                setViewAction(CardCollectionViewerAction.ShowSharedCode(code))
            }
        }
    }

    private fun processOnCardClick(card: CardDTO) {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.OpenCardToEdit(card = card))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.NavigateBack)
        }
    }

    private fun processPlayGame() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CardCollectionViewerState.CollectionCards) return@withViewModelScope

            setViewAction(CardCollectionViewerAction.PlayGame(state.collection.id))
        }
    }
}
