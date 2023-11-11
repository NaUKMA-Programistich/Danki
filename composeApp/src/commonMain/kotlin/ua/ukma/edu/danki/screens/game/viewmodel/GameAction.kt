package ua.ukma.edu.danki.screens.game.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class GameAction {
    data class ShowDefinition(val card: CardDTO) : GameAction()
    data class ShowGameResult(val gameCards: List<CardDTO>, val gameResults: List<Boolean>) : GameAction()
}