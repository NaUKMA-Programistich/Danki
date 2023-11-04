package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel


class EditCardViewModel() : ViewModel<EditCardState, EditCardAction, EditCardEvent>(initialState = EditCardState.Loading) {
    override fun obtainEvent(viewEvent: EditCardEvent) {
        when (viewEvent) {
            else -> {}
        }
    }


}