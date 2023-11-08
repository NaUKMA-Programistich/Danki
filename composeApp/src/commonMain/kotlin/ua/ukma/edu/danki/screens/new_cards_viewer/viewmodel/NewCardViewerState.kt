package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.new_cards_viewer.model.NewCardViewerModel

sealed class NewCardViewerState {

    data class NewCardCards (val newCardViewerModelMap: Map<UserCardCollectionDTO?,MutableList<NewCardViewerModel>>) : NewCardViewerState()

    data object Loading : NewCardViewerState()


}