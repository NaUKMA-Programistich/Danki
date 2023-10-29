package ua.ukma.edu.danki.screens.search_history.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel

class SearchHistoryViewModel() :
    ViewModel<SearchHistoryState, SearchHistoryAction, SearchHistoryEvent>(initialState = SearchHistoryState.Loading) {
    override fun obtainEvent(viewEvent: SearchHistoryEvent) {
        when (viewEvent) {
            is SearchHistoryEvent.SelectWord -> processSelectWord(viewEvent.word)
            is SearchHistoryEvent.GoBack -> processGoBack()
        }
    }

    init {
        withViewModelScope {
            val searchedWords = listOf("word1", "word2", "word3") // TODO("replace with real data")
            setViewState(SearchHistoryState.SearchHistoryList(searchedWords))
        }
    }

    private fun processSelectWord(word: String) {
        withViewModelScope {
            setViewAction(SearchHistoryAction.OpenDefinition(word))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(SearchHistoryAction.NavigateBack)
        }
    }

}