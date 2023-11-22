package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class CardCollectionViewerAction {

    data class OpenCardToEdit(val card: CardDTO) : CardCollectionViewerAction()
    data object NavigateBack : CardCollectionViewerAction()

}