package ua.ukma.edu.danki.screens.game.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class GameState {
    data class GameInProgress(
        val gameCards: List<CardDTO>,
        val gameResults: MutableList<Boolean>,
        val currentCardIndex: Int,
        val currentCard: String,
        val score: Int
    ) : GameState()

    data object Loading : GameState()
}