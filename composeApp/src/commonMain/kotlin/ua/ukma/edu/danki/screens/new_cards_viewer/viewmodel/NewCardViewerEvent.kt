package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.models.CardDTO


sealed class NewCardViewerEvent {
    data class OnCardClick (val newCard: CardDTO) : NewCardViewerEvent()
    data object GoBack : NewCardViewerEvent()
}