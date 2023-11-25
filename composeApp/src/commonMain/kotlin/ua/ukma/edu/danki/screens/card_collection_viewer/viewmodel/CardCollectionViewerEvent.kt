package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CardCollectionViewerEvent {
    data class OnCardClick (val card: CardDTO) : CardCollectionViewerEvent()
    data object GoBack : CardCollectionViewerEvent()
    data class DeleteCollection (val collection: UserCardCollectionDTO) : CardCollectionViewerEvent()

    data object PlayGame : CardCollectionViewerEvent()
}