package ua.ukma.edu.danki.screens.game_results.viewmodel

import ua.ukma.edu.danki.models.CardDTO

sealed class GameResultsState {
    data class ShowGameResults(
        val gameCardsAndResults: List<Pair<CardDTO, Boolean>>,
    ) : GameResultsState()

    data object Loading : GameResultsState()
}