package ua.ukma.edu.danki.screens.edit_card_screen

import androidx.compose.runtime.*
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import ua.ukma.edu.danki.screens.edit_card_screen.ui.CardToEditView
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardAction
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardState
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardViewModel
import kotlin.time.Duration.Companion.hours

@Composable
fun EditCardScreen (editCard : EditCard = EditCard(
    term = "",
    definition = "",
    collection = UserCardCollectionDTO(id = "", name = "" , lastModified = Clock.System.now().minus(1.hours) , own = false , favorite = false)
)) {
    StoredViewModel(factory = { EditCardViewModel(editCard = editCard) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is EditCardState.CardToEdit -> CardToEditView(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            is EditCardState.Loading -> {

            }
        }

        when (val action = viewAction) {
            is EditCardAction.NavigateBack -> {
                navController.popBackStack()
            }
            else -> {

            }
        }
    }
}