package ua.ukma.edu.danki.screens.collections.viewmodel

import ua.ukma.edu.danki.models.CollectionSortParam
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class CollectionEvent {
    data object ShowAll : CollectionEvent()
    data class SortList(val sortParam: CollectionSortParam) : CollectionEvent()
    data object ChangeSortingOrder : CollectionEvent()
    data object ShowOnlyFavorites : CollectionEvent()
    data class ChangeFavoriteStatus(val id: String) : CollectionEvent()
    data class OpenCollection(val collection: UserCardCollectionDTO) : CollectionEvent()
    data class CreateCollection(val collectionName: String) : CollectionEvent()
    data class UpdateCollection(val collection: UserCardCollectionDTO) : CollectionEvent()
    data class ChangeCollectionName(val collection: UserCardCollectionDTO) : CollectionEvent()
    data class DeleteCollection(val collectionId: String) : CollectionEvent()
    data object DeleteSelected : CollectionEvent()
    data object ShowCreateCollectionDialog : CollectionEvent()
    data class ToggleSelectCollection(val collectionId: String) : CollectionEvent()
    data object OpenGetSharedCodeDialog : CollectionEvent()
    data class GetSharedCode(val code: Long) : CollectionEvent()
}