package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.CardSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent

sealed class CardCollectionViewerEvent {
    data class OnCardClick(val card: CardDTO) : CardCollectionViewerEvent()
    data object GoBack : CardCollectionViewerEvent()
    data class DeleteCollection(val collection: UserCardCollectionDTO) : CardCollectionViewerEvent()
    data class SortList(val sortParam: CardSortParam) : CardCollectionViewerEvent()

    data object ShareCollection : CardCollectionViewerEvent()
    data class ChangeSortingOrder  (val orderIsAscending : Boolean) : CardCollectionViewerEvent()
    data object PlayGame : CardCollectionViewerEvent()
}