package ua.ukma.edu.danki.screens.definition.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class DefinitionEvent {
    data object GoBack : DefinitionEvent()

    data class OnNewCardClick(val newCard: CardDTO) : DefinitionEvent()

    data class SelecteDefinition(val index: Int) : DefinitionEvent()
}