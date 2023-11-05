package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardCollectionViewerModel
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel

sealed class CardCollectionViewerEvent {
    data class OnCardClick (val cardViewerModel: CardViewerModel) : CardCollectionViewerEvent()
    data object GoBack : CardCollectionViewerEvent()
    data class DeleteCollection (val cardCollectionViewerModel: CardCollectionViewerModel) : CardCollectionViewerEvent()
}