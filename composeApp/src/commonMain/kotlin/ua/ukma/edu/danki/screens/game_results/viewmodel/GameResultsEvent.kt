package ua.ukma.edu.danki.screens.game_results.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class GameResultsEvent {
    data object CloseResults : GameResultsEvent()
    data class ShowDefinition(val card: CardDTO) : GameResultsEvent()
}