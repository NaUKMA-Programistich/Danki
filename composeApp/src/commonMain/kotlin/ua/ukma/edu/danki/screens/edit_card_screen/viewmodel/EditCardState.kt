package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard

sealed class EditCardState {

    data class CardToEdit (val editCard: EditCard, val collectionList: List<UserCardCollectionDTO>) : EditCardState()

    data object Loading : EditCardState()


}