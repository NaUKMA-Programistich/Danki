package ua.ukma.edu.danki.screens.search.viewmodel

sealed class SearchAction {
    data object OpenHistory : SearchAction()
    data class OpenDefinition(val word: String) : SearchAction()
}