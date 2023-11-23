package ua.ukma.edu.danki.screens.definition.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.dictionary.DictionaryRepository
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition

class DefinitionViewModel(term: String, dictionaryRepository: DictionaryRepository = Injection.dictionaryRepository) :
    ViewModel<DefinitionState, DefinitionAction, DefinitionEvent>(initialState = DefinitionState.Loading) {
    override fun obtainEvent(viewEvent: DefinitionEvent) {
        when (viewEvent) {
            DefinitionEvent.GoBack -> processGoBack()
            is DefinitionEvent.OnNewCardClick -> processOnNewCard(newCard = viewEvent.newCard)
        }
    }

    init {
        withViewModelScope {
            val termDefinition = dictionaryRepository.getTermDefinition(
                GetTermDefinition(
                    term = term
                )
            )
            // TODO: make this display multiple defintions
            val definition = termDefinition?.term?.data?.first()?.definition ?: ""
            setViewState(DefinitionState.TermDefinition(term, definition))
        }
    }

    private fun processOnNewCard(newCard : CardDTO) {
        withViewModelScope {
            setViewAction(DefinitionAction.OnNewCard(newCard))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(DefinitionAction.NavigateBack)
        }
    }

}