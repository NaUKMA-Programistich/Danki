package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.screens.new_cards_viewer.model.NewCardViewerModel

sealed class NewCardViewerEvent {
    data class OnCardClick (val newCardViewerModel: NewCardViewerModel) : NewCardViewerEvent()
    data object GoBack : NewCardViewerEvent()
}