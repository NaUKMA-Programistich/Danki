package ua.ukma.edu.danki.screens.edit_card_screen.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import kotlin.time.Duration.Companion.hours


class EditCardViewModel(editCard: EditCard) : ViewModel<EditCardState, EditCardAction, EditCardEvent>(initialState = EditCardState.Loading) {
    override fun obtainEvent(viewEvent: EditCardEvent) {
        when (viewEvent) {
            EditCardEvent.Cancel -> processCancel()
            is EditCardEvent.SaveCard -> processSavingCard(viewEvent.editCard)
            is EditCardEvent.DeleteCard -> processDeletingCard(viewEvent.editCard)
        }
    }

    private fun processDeletingCard(editCard: EditCard) {
        withViewModelScope {
            // TODO("delete card")
        }
    }

    private fun processSavingCard(editCard: EditCard) {
        withViewModelScope {
            // TODO("save card in db")
        }
    }

    init {
        withViewModelScope {
            setViewState(EditCardState.CardToEdit(editCard = editCard, collectionList = mockData))
        }
    }

    private fun processCancel() {
        withViewModelScope {
            setViewAction(EditCardAction.NavigateBack)
        }
    }

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


}