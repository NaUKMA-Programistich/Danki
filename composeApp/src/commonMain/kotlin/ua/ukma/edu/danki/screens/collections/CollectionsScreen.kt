package ua.ukma.edu.danki.screens.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.adeo.kviewmodel.compose.observeAsState
import com.adeo.kviewmodel.odyssey.StoredViewModel
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.modal_navigation.AlertConfiguration
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.core.composable.ComposableLoading
import ua.ukma.edu.danki.navigation.NavigationRoute
import ua.ukma.edu.danki.screens.collections.ui.CollectionViewList
import ua.ukma.edu.danki.screens.collections.ui.CreateCollectionDialog
import ua.ukma.edu.danki.screens.collections.ui.GetSharedCodeDialog
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionAction
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionEvent
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionState
import ua.ukma.edu.danki.screens.collections.viewmodel.CollectionViewModel

@Composable
internal fun CollectionsScreen() {
    StoredViewModel(factory = { CollectionViewModel() }) { viewModel ->
        val navController = LocalRootController.current
        val modalController = navController.findModalController()
        val alertConfiguration = AlertConfiguration(maxHeight = 0.45f, maxWidth = 0.6f, cornerRadius = 8)
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
                navController.launch(
                    screen = NavigationRoute.CardCollectionViewer.name,
                    params = action.collection,
                    animationType = AnimationType.Present(animationTime = 500)
                )
            }

            is CollectionAction.ShowCreateCollectionDialog -> modalController.present(alertConfiguration) { key ->
                CreateCollectionDialog(
                    onCloseClick = { modalController.popBackStack(key) },
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            is CollectionAction.ShowChangeCollectionNameDialog -> modalController.present(alertConfiguration) { key ->
                CreateCollectionDialog(
                    action.collection,
                    onCloseClick = { modalController.popBackStack(key) },
                    onEvent = { viewModel.obtainEvent(it) }
                )
            }

            is CollectionAction.ShowGetSharedCodeDialog -> modalController.present(alertConfiguration) { key ->
                GetSharedCodeDialog(
                    onSubmit = {
                        viewModel.obtainEvent(CollectionEvent.GetSharedCode(it))
                        modalController.popBackStack(key)
                    }
                )
            }

            else -> {

            }
        }
    }
}