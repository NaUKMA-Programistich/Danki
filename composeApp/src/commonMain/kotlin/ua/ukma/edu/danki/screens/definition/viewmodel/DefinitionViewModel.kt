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
            is DefinitionEvent.SelecteDefinition -> processSelectDefinition(index = viewEvent.index)
        }
    }

    init {
        withViewModelScope {
            val termDefinition = dictionaryRepository.getTermDefinition(
                GetTermDefinition(
                    term = term
                )
            )
            val definitions = termDefinition?.term?.data ?: emptyList()
            setViewState(DefinitionState.TermDefinition(term, definitions))
        }
    }

    private fun processOnNewCard(newCard: CardDTO) {
        withViewModelScope {
            setViewAction(DefinitionAction.OnNewCard(newCard))
        }
    }

    private fun processSelectDefinition(index: Int) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is DefinitionState.TermDefinition) return@withViewModelScope

            setViewState(DefinitionState.TermDefinition(state.term, state.definitions, index))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(DefinitionAction.NavigateBack)
        }
    }

}