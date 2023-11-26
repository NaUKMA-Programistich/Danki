package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.GetCardsOfCollection

class NewCardViewerViewModel(
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository,
    private val cardRepository: CardRepository = Injection.cardRepository
) : ViewModel<NewCardViewerState, NewCardViewerAction, NewCardViewerEvent>(initialState = NewCardViewerState.Loading) {

    init {
        val sd = 0
    }

    override fun obtainEvent(viewEvent: NewCardViewerEvent) {
        when (viewEvent) {
            NewCardViewerEvent.GoBack -> processGoBack()
            is NewCardViewerEvent.OnCardClick -> processOnCardClick(viewEvent.newCard)
        }
    }

    init {
        withViewModelScope {
            setViewState(NewCardViewerState.Loading)
            setViewState(NewCardViewerState.NewCardCards(newCards = getNewCards()))
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