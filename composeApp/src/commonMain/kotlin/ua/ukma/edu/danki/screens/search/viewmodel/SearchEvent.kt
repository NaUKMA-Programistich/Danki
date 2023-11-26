package ua.ukma.edu.danki.screens.search.viewmodel

sealed class SearchEvent {
    data class ChangeInput(val newInput: String) : SearchEvent()
    data class SelectWord(val word: String) : SearchEvent()
    data object CreateNewCardHistory : SearchEvent()
}