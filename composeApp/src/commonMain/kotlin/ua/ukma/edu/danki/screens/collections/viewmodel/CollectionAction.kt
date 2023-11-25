package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CollectionAction {
    data class OpenCollection(val collection: UserCardCollectionDTO) : CollectionAction()
    data object ShowCreateCollectionDialog : CollectionAction()
    data class ShowChangeCollectionNameDialog(val collection: UserCardCollectionDTO) : CollectionAction()
    data object ShowGetSharedCodeDialog : CollectionAction()
}