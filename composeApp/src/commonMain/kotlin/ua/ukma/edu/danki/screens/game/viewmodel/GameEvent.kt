package ua.ukma.edu.danki.screens.game.viewmodel

sealed class GameEvent {
    data class NextCard(val previousWasCorrect: Boolean) : GameEvent()
    data object ShowDefinition : GameEvent()
}