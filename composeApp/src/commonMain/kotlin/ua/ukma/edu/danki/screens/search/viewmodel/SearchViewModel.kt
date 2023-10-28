package ua.ukma.edu.danki.screens.search.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel

class SearchViewModel() : ViewModel<SearchState, SearchAction, SearchEvent>(initialState = SearchState.Loading) {
    override fun obtainEvent(viewEvent: SearchEvent) {
        when (viewEvent) {
            is SearchEvent.ChangeInput -> processChangeInput(viewEvent.newInput)
            is SearchEvent.SelectWord -> processSelectWord(viewEvent.word)
            is SearchEvent.DisplayHistory -> processDisplayHistory()
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

            val results = List(newInput.length) { ('a'..'z').random().toString() }  // TODO("get real results")
            setViewState(SearchState.SearchResults(newInput, results))
        }
    }

    private fun processSelectWord(word: String) {
        withViewModelScope {
            setViewAction(SearchAction.OpenDefinition(word))
        }
    }

    private fun processDisplayHistory() {
        withViewModelScope {
            setViewAction(SearchAction.OpenHistory)
        }
    }


}