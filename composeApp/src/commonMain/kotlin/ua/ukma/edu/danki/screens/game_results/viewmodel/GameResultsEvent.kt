package ua.ukma.edu.danki.screens.game_results.viewmodel

sealed class GameResultsEvent {
    data object CloseResults : GameResultsEvent()
}