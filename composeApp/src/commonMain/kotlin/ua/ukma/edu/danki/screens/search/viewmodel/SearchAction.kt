package ua.ukma.edu.danki.screens.search.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class SearchAction {
    data class CreateNewCard (val newCard : CardDTO) : SearchAction()
    data class OpenDefinition(val word: String) : SearchAction()
}