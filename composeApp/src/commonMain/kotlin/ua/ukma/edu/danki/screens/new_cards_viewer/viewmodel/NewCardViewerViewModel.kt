package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.*

class NewCardViewerViewModel(
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository,
    private val cardRepository: CardRepository = Injection.cardRepository
) : ViewModel<NewCardViewerState, NewCardViewerAction, NewCardViewerEvent>(initialState = NewCardViewerState.Loading) {


    override fun obtainEvent(viewEvent: NewCardViewerEvent) {
        when (viewEvent) {
            NewCardViewerEvent.GoBack -> processGoBack()
            is NewCardViewerEvent.OnCardClick -> processOnCardClick(viewEvent.newCard)
            NewCardViewerEvent.OpenAddAllDialog -> processOnOpenAddAllDialog()
            is NewCardViewerEvent.AddAllRecentsToCollection -> processAddAllRecentsToCollection(viewEvent.collection)
        }
    }

    init {
        withViewModelScope {
            setViewState(NewCardViewerState.Loading)
            setViewState(NewCardViewerState.NewCardCards(newCards = getNewCards()))
        }
    }

    private fun processAddAllRecentsToCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            val recentUUID = cardCollectionsRepository.getRecentCollection()?.id ?: return@withViewModelScope
            val listOfRecentCards = cardRepository.getCardsOfCollection(GetCardsOfCollection(collection = recentUUID))
            listOfRecentCards?.cards?.forEach { card ->
                card.id?.let {
                    cardRepository.moveCardToCollection(MoveCardToCollectionRequest(it, collection.id))
                }
            }
            setViewState(NewCardViewerState.NewCardCards(newCards = getNewCards()))
        }
    }

    private fun processOnOpenAddAllDialog() {
        withViewModelScope {
            setViewAction(
                NewCardViewerAction.ShowAddAllDialog(
                    cardCollectionsRepository.getUserCollections(GetUserCollections())?.cardCollections ?: emptyList()
                )
            )
        }
    }

    private suspend fun getNewCards(): List<CardDTO> {
        val recentUUID = cardCollectionsRepository.getRecentCollection()?.id ?: return emptyList()
        val listOfCards = cardRepository.getCardsOfCollection(GetCardsOfCollection(collection = recentUUID))
        return listOfCards?.cards ?: emptyList()
    }

    private fun processOnCardClick(newCard: CardDTO) {
        withViewModelScope {
            setViewAction(NewCardViewerAction.OpenCardToEdit(newCard = newCard))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(NewCardViewerAction.NavigateBack)
        }
    }

}