package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO

sealed class EditCardState {

    data class CardToEdit (val card: CardDTO, val collectionList: List<UserCardCollectionDTO>) : EditCardState()

    data object Loading : EditCardState()


}