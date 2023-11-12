package ua.ukma.edu.danki.screens.game_results.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class GameResultsAction {
    data object CloseResults : GameResultsAction()
    data class ShowDefinition(val card: CardDTO) : GameResultsAction()
}