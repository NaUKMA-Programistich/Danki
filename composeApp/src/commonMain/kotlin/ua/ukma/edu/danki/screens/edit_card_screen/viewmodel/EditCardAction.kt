package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import ua.ukma.edu.danki.screens.edit_card_screen.data.CardEditModel

sealed class EditCardAction {

    data class SaveCard (val cardEditModel: CardEditModel) : EditCardAction()

    data object NavigateBack : EditCardAction()

}