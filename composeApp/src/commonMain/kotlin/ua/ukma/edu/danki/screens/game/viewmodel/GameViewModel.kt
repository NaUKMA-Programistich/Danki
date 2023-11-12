package ua.ukma.edu.danki.screens.game.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CardDTO
import kotlin.time.Duration.Companion.hours

class GameViewModel(collectionId: String, cardsAmountToPlay: Int = -1) :
    ViewModel<GameState, GameAction, GameEvent>(initialState = GameState.Loading) {
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
            //TODO("collecting real data from db/server")
            val allCards = mockData
            val cardsToPlay = allCards.shuffled().take(
                if (cardsAmountToPlay < 1 || cardsAmountToPlay > allCards.size) allCards.size
                else cardsAmountToPlay
            )

            setViewState(
                GameState.GameInProgress(
                    cardsToPlay, MutableList(cardsToPlay.size) { false },
                    currentCardIndex = 0, cardsToPlay[0].term, score = 0
                )
            )
        }
    }

    override fun obtainEvent(viewEvent: GameEvent) {
        when (viewEvent) {
            is GameEvent.NextCard -> onNextCard(viewEvent.previousWasCorrect)
            GameEvent.FinishGame -> finishGame()
            GameEvent.ShowDefinition -> onShowDefinition()
        }
    }

    private fun onNextCard(previousWasCorrect: Boolean) {
        withViewModelScope {
            val state = viewStates().value
            if (state !is GameState.GameInProgress) return@withViewModelScope

            val nextIndex = state.currentCardIndex + 1
            if (nextIndex >= state.gameCards.size)
                finishGame()
            else {
                state.gameResults[state.currentCardIndex] = previousWasCorrect
                setViewState(
                    GameState.GameInProgress(
                        state.gameCards, state.gameResults,
                        nextIndex, state.gameCards[nextIndex].term, state.gameResults.filter { it }.size
                    )
                )
            }
        }
    }

    private fun finishGame() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is GameState.GameInProgress) return@withViewModelScope

            setViewAction(GameAction.ShowGameResult(state.gameCards, state.gameResults))
        }
    }

    private fun onShowDefinition() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is GameState.GameInProgress) return@withViewModelScope

            setViewAction(GameAction.ShowDefinition(state.gameCards[state.currentCardIndex]))
        }
    }


}
