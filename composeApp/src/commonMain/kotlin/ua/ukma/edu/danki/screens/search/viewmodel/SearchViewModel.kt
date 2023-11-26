package ua.ukma.edu.danki.screens.search.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.dictionary.DictionaryRepository
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions

class SearchViewModel(
    private val dictionaryRepository: DictionaryRepository = Injection.dictionaryRepository
) : ViewModel<SearchState, SearchAction, SearchEvent>(initialState = SearchState.Loading) {
    override fun obtainEvent(viewEvent: SearchEvent) {
        when (viewEvent) {
            is SearchEvent.ChangeInput -> processChangeInput(viewEvent.newInput)
            is SearchEvent.SelectWord -> processSelectWord(viewEvent.word)
            is SearchEvent.CreateNewCardHistory -> processCreateNewCard()
        }
    }

    init {
        withViewModelScope {
            setViewState(SearchState.SearchResults("", emptyList()))
        }
    }

    private fun processChangeInput(newInput: String) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is SearchState.SearchResults) return@withViewModelScope

            setViewState(SearchState.SearchResults(newInput, state.results))

            val dictionarySuggestions = dictionaryRepository.getDictionarySuggestion(
                GetDictionarySuggestions(
                    input = newInput,
                    count = 5
                )
            )
            setResults(dictionarySuggestions?.suggestions?.map { it.term } ?: emptyList())
        }
    }

    private fun setResults(results: List<String>) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is SearchState.SearchResults) return@withViewModelScope
            setViewState(SearchState.SearchResults(state.input, results))
        }
    }

    private fun processSelectWord(word: String) {
        withViewModelScope {
            setViewAction(SearchAction.OpenDefinition(word))
        }
    }

    private fun processCreateNewCard() {
        withViewModelScope {
            setViewAction(SearchAction.CreateNewCard (CardDTO (term = "", definition = "")))
        }
    }


}