package ua.ukma.edu.danki.screens.search.viewmodel

sealed class SearchState {
    data class SearchResults(
        val input: String,
        val results: List<String>
    ): SearchState()
    data object Loading : SearchState()
}