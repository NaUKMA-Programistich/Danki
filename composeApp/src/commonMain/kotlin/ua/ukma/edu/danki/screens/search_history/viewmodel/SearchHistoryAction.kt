package ua.ukma.edu.danki.screens.search_history.viewmodel

sealed class SearchHistoryAction {
    data class OpenDefinition(val word: String) : SearchHistoryAction()
    data object NavigateBack : SearchHistoryAction()
}