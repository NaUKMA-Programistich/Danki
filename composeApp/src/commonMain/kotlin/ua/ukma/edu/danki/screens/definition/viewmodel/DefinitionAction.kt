package ua.ukma.edu.danki.screens.definition.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class DefinitionAction {
    data object NavigateBack : DefinitionAction()

    data class OnNewCard (val newCard: CardDTO) : DefinitionAction()
}