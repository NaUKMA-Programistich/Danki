package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class NewCardViewerAction {

    data class OpenCardToEdit(val newCard: CardDTO) : NewCardViewerAction()
    data object NavigateBack : NewCardViewerAction()

}