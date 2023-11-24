package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import kotlin.time.Duration.Companion.hours


class EditCardViewModel(card : CardDTO) : ViewModel<EditCardState, EditCardAction, EditCardEvent>(initialState = EditCardState.Loading) {

    private val mockData = listOf(
        UserCardCollectionDTO(
            "UniqueID1", "Study", Clock.System.now().minus(10.hours),
            own = true, true
        ),
        UserCardCollectionDTO(
            "UniqueID2", "School", Clock.System.now().minus(40.hours),
            own = true, false
        ),
        UserCardCollectionDTO(
            "UniqueID3", "Work", Clock.System.now().minus(100.hours),
            own = true, false
        ),
        UserCardCollectionDTO(
            "UniqueID4", "Life", Clock.System.now().minus(1.hours),
            own = false, true
        ),
        UserCardCollectionDTO(
            "UniqueID5", "Family", Clock.System.now().minus(1.hours),
            own = false, true
        ),
    )

    override fun obtainEvent(viewEvent: EditCardEvent) {
        when (viewEvent) {
            EditCardEvent.Cancel -> processCancel()
            is EditCardEvent.SaveCard -> processSavingCard(card = viewEvent.card,collectionId = viewEvent.collectionId)
            is EditCardEvent.DeleteCard -> processDeletingCard(card = viewEvent.card)
        }
    }

    private fun processDeletingCard(card: CardDTO) {
        withViewModelScope {
            // TODO("delete card")
        }
    }

    private fun processSavingCard(card: CardDTO, collectionId: String?) {
        withViewModelScope {
            // TODO("save card in db")
        }
    }

    init {
        withViewModelScope {
           setViewState(EditCardState.Loading)
           setViewState(EditCardState.CardToEdit(card = card, collectionList = mockData))
        }
    }

    private fun processCancel() {
        withViewModelScope {
            setViewAction(EditCardAction.NavigateBack)
        }
    }



}