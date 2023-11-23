package ua.ukma.edu.danki.screens.edit_add_card_screen

import androidx.compose.runtime.*
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.screens.edit_add_card_screen.ui.CardToEditAddView
import ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel.EditAddCardAction
import ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel.EditAddCardState
import ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel.EditAddCardViewModel

@Composable
fun EditAddCardScreen (card : CardDTO, isNew : Boolean) {
    StoredViewModel(factory = { EditAddCardViewModel(card = card) }) { viewModel ->
        val navController = LocalRootController.current
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is EditAddCardState.CardToEdit -> {
                CardToEditAddView(
                    state = state,
                    onEvent = { viewModel.obtainEvent(it) },
                    isNew = isNew
                )
            }

            is EditAddCardState.Loading -> {
                ComposableLoading()
            }
        }

        when (val action = viewAction) {
            is EditAddCardAction.NavigateBack -> {
                navController.popBackStack()
            }
            else -> {

            }
        }
    }
}