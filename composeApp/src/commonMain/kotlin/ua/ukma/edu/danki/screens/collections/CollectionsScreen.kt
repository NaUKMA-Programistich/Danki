package ua.ukma.edu.danki.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.screens.collections.ui.CollectionViewList
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionViewModel

@Composable
internal fun CollectionsScreen() {
    StoredViewModel(factory = { CollectionViewModel() }) { viewModel ->
        val viewState by viewModel.viewStates().observeAsState()

        when (val state = viewState) {
            is CollectionState.CollectionList -> CollectionViewList(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            CollectionState.Loading -> ComposableLoading()
        }
    }
}