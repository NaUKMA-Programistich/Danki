package ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel

import ua.ukma.edu.danki.models.CardDTO


sealed class EditAddCardEvent {

    data class DeleteCard (val card: CardDTO) : EditAddCardEvent()
    data class SaveCard (val card: CardDTO , val collectionId : String? , val isNew : Boolean = false) : EditAddCardEvent()

    data object Cancel : EditAddCardEvent()
}