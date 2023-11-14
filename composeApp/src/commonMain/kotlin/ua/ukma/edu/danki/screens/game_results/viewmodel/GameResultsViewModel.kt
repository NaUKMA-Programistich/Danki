package ua.ukma.edu.danki.screens.game_results.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CardDTO

class GameResultsViewModel(cards: List<CardDTO>, gameResults: List<Boolean>) :
    ViewModel<GameResultsState, GameResultsAction, GameResultsEvent>(initialState = GameResultsState.Loading) {
    init {
        withViewModelScope {
            setViewState(
                GameResultsState.ShowGameResults(cards zip gameResults)
            )
        }
    }

    override fun obtainEvent(viewEvent: GameResultsEvent) {
        when (viewEvent) {
            is GameResultsEvent.CloseResults -> onCloseResults()
        }
    }

    private fun onCloseResults() {
        withViewModelScope {
            setViewAction(GameResultsAction.CloseResults)
        }
    }
}
