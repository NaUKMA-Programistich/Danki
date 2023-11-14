package ua.ukma.edu.danki.screens.definition.viewmodel

sealed class DefinitionState {
    data class TermDefinition(
        val term: String,
        val definition: String
    ) : DefinitionState()

    data object Loading : DefinitionState()
}