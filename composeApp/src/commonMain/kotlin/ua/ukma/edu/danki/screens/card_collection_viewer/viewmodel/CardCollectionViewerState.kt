package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardCollectionViewerModel
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel


sealed class CardCollectionViewerState {

    data class CollectionCards (val cardCollectionViewerModel : CardCollectionViewerModel, val cardViewerModelList: List<CardViewerModel>) : CardCollectionViewerState()

    data object Loading : CardCollectionViewerState()


}