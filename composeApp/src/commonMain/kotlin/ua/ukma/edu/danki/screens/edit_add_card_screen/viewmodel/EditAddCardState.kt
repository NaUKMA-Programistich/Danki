package ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class EditAddCardState {

    data class CardToEdit (val card: CardDTO, val collectionList: List<UserCardCollectionDTO>) : EditAddCardState()

    data object Loading : EditAddCardState()


}