package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel

sealed class CardCollectionViewerAction {

    data class OpenCardToEdit(val cardViewerModel: CardViewerModel) : CardCollectionViewerAction()
    data object NavigateBack : CardCollectionViewerAction()

}