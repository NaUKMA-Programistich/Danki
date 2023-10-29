package ua.ukma.edu.danki.screens.definition.viewmodel

sealed class DefinitionEvent {
    data object GoBack : DefinitionEvent()
}