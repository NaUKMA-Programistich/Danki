package ua.ukma.edu.danki.screens.search_history.viewmodel

sealed class SearchHistoryState {
    data class SearchHistoryList(
        val words: List<String>
    ) : SearchHistoryState()

    data object Loading : SearchHistoryState()
}