package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CardCollectionViewerState {

    data class CollectionCards (val collection : UserCardCollectionDTO, val cardList: List<CardDTO>) : CardCollectionViewerState()

    data object Loading : CardCollectionViewerState()


}