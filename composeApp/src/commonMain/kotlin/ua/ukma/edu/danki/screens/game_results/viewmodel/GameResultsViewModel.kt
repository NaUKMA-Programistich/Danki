package ua.ukma.edu.danki.screens.game_results.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CardDTO
import kotlin.time.Duration.Companion.hours

class GameResultsViewModel(cards: List<CardDTO>, gameResults: List<Boolean>) :
    ViewModel<GameResultsState, GameResultsAction, GameResultsEvent>(initialState = GameResultsState.Loading) {
    private val mockData: List<CardDTO> = listOf(
        CardDTO(
            1L, "Book", "Knowledge on paper",
            Clock.System.now(), Clock.System.now().minus(10.hours)
        ),
        CardDTO(
            2L, "Book2", "Knowledge on paper2",
            Clock.System.now(), Clock.System.now().minus(10.hours)
        ),
        CardDTO(
            3L, "Book3", "Knowledge on paper3",
            Clock.System.now(), Clock.System.now().minus(10.hours)
        ),
        CardDTO(
            4L, "Book4", "Knowledge on paper4",
            Clock.System.now(), Clock.System.now().minus(10.hours)
        ),
        CardDTO(
            5L, "Book5", "Knowledge on paper5",
            Clock.System.now(), Clock.System.now().minus(10.hours)
        ),
    )

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
