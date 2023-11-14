package ua.ukma.edu.danki.screens.definition.viewmodel

sealed class DefinitionAction {
    data object NavigateBack : DefinitionAction()
}