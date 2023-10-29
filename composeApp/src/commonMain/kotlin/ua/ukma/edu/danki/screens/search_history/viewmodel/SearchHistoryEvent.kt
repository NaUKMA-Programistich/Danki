package ua.ukma.edu.danki.screens.search_history.viewmodel

sealed class SearchHistoryEvent {
    data class SelectWord(val word: String) : SearchHistoryEvent()
    data object GoBack : SearchHistoryEvent()
}