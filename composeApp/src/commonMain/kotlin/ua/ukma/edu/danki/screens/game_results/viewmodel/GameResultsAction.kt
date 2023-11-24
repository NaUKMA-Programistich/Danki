package ua.ukma.edu.danki.screens.game_results.viewmodel

sealed class GameResultsAction {
    data object CloseResults : GameResultsAction()
}