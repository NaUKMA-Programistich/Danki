package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.screens.new_cards_viewer.model.NewCardViewerModel

sealed class NewCardViewerAction {

    data class OpenCardToEdit(val newCardViewerModel: NewCardViewerModel) : NewCardViewerAction()
    data object NavigateBack : NewCardViewerAction()

}