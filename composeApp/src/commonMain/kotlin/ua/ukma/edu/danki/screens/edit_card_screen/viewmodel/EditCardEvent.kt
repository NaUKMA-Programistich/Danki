package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import ua.ukma.edu.danki.models.CardDTO


sealed class EditCardEvent {

    data class DeleteCard (val card: CardDTO) : EditCardEvent()
    data class SaveCard (val card: CardDTO , val collectionId : String?) : EditCardEvent()
    data object Cancel : EditCardEvent()
}