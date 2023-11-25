package ua.ukma.edu.danki.screens.game.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.models.GetCardsOfCollection

class GameViewModel(
    collectionId: String,
    cardsAmountToPlay: Int = -1,
    cardRepository: CardRepository = Injection.cardRepository
) :
    ViewModel<GameState, GameAction, GameEvent>(initialState = GameState.Loading) {

    init {
        withViewModelScope {
            val listOfCards = cardRepository.getCardsOfCollection(
                GetCardsOfCollection(
                    collection = collectionId
                )
            )

            val allCards = listOfCards?.cards ?: emptyList()
            val cardsToPlay = allCards.shuffled().take(
                if (cardsAmountToPlay < 1 || cardsAmountToPlay > allCards.size) allCards.size
                else cardsAmountToPlay
            )

            if (cardsToPlay.isEmpty())
                setViewAction(GameAction.ShowGameResult(emptyList(), emptyList()))
            else
                setViewState(
                    GameState.GameInProgress(
                        cardsToPlay, MutableList(cardsToPlay.size) { false },
                        currentCardIndex = 0, cardsToPlay[0].term, score = 0, showDefinition = false
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

            state.gameResults[state.currentCardIndex] = previousWasCorrect

            val nextIndex = state.currentCardIndex + 1
            if (nextIndex >= state.gameCards.size)
                finishGame()
            else
                setViewState(
                    GameState.GameInProgress(
                        state.gameCards, state.gameResults,
                        nextIndex, state.gameCards[nextIndex].term, state.gameResults.filter { it }.size,
                        showDefinition = false
                    )
                )
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

            setViewState(
                GameState.GameInProgress(
                    state.gameCards,
                    state.gameResults,
                    state.currentCardIndex,
                    if (!state.showDefinition) state.gameCards[state.currentCardIndex].definition
                    else state.gameCards[state.currentCardIndex].term,
                    state.gameResults.filter { it }.size,
                    !state.showDefinition
                )
            )
        }
    }
}
