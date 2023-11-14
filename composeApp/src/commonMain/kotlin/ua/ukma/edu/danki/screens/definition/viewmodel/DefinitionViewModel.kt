package ua.ukma.edu.danki.screens.definition.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel

class DefinitionViewModel(term: String) :
    ViewModel<DefinitionState, DefinitionAction, DefinitionEvent>(initialState = DefinitionState.Loading) {
    override fun obtainEvent(viewEvent: DefinitionEvent) {
        when (viewEvent) {
            DefinitionEvent.GoBack -> processGoBack()
        }
    }

    init {
        withViewModelScope {
            val definition = List(30) { _ -> "text" }.joinToString(separator = "") // TODO("replace with real data")
            setViewState(DefinitionState.TermDefinition(term, definition))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(DefinitionAction.NavigateBack)
        }
    }

}