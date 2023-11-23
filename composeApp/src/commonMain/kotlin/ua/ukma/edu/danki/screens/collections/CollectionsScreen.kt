package ua.ukma.edu.danki.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.extensions.push
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.collections.ui.CollectionViewList
import ua.ukma.edu.danki.screens.collections.ui.CreateCollectionDialog
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionAction
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionViewModel

@Composable
internal fun CollectionsScreen() {
    StoredViewModel(factory = { CollectionViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val modalController = navController.findModalController()
        val alertConfiguration = AlertConfiguration(maxHeight = 0.5f, maxWidth = 0.7f, cornerRadius = 8)
        val viewState by viewModel.viewStates().observeAsState()
        val viewAction by viewModel.viewActions().observeAsState()

        when (val state = viewState) {
            is CollectionState.CollectionList -> CollectionViewList(
                state = state,
                onEvent = { viewModel.obtainEvent(it) }
            )

            CollectionState.Loading -> ComposableLoading()
        }

        when (val action = viewAction) {
            is CollectionAction.OpenCollection -> {
                navController.push(NavigationRoute.CardCollectionViewer.name)
            }

            is CollectionAction.ShowCreateCollectionDialog -> modalController.present(alertConfiguration) { key ->
                CreateCollectionDialog(
                    onCloseClick = { modalController.popBackStack(key) },
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            else -> {

            }
        }
    }
}