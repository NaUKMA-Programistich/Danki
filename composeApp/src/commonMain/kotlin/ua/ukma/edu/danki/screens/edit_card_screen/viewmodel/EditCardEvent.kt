package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard


sealed class EditCardEvent {

    data class DeleteCard (val editCard: EditCard) : EditCardEvent()
    data class SaveCard (val editCard: EditCard) : EditCardEvent()
    data object Cancel : EditCardEvent()
}