package ua.ukma.edu.danki.screens.definition.viewmodel

import ua.ukma.edu.danki.models.dictionary.UnwrappedData

sealed class DefinitionState {
    data class TermDefinition(
        val term: String,
        val definitions: List<UnwrappedData>,
        val selectedDefinitionIndex: Int = 0
    ) : DefinitionState()

    data object Loading : DefinitionState()
}