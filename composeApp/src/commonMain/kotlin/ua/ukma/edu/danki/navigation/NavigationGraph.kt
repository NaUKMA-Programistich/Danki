package ua.ukma.edu.danki.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.core.animations.AnimationType
import ua.ukma.edu.danki.screens.card_collection_viewer.CardCollectionViewerScreen
import ua.ukma.edu.danki.screens.login.LoginScreen
import ua.ukma.edu.danki.screens.new_cards_viewer.NewCardViewerScreen
import ua.ukma.edu.danki.screens.definition.DefinitionScreen
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.game.GameScreen
import ua.ukma.edu.danki.screens.game_results.GameResultsScreen
import ua.ukma.edu.danki.screens.collections.CollectionsScreen
import ua.ukma.edu.danki.screens.edit_add_card_screen.EditAddCardScreen
import ua.ukma.edu.danki.screens.register.RegistrationScreen
import ua.ukma.edu.danki.screens.search.SearchScreen
import ua.ukma.edu.danki.screens.search_history.SearchHistoryScreen


@Composable
private fun SideNavigation(selectedElem: MutableState<Int>, content: @Composable () -> Unit) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            val navController = LocalRootController.current
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {
                    if (navController.currentScreen.value?.screen?.realKey == NavigationRoute.Search.name || navController.currentScreen.value?.screen?.realKey == NavigationRoute.Collections.name) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                navController.launch(
                                    screen = NavigationRoute.NewCardViewer.name,
                                    animationType = AnimationType.Present(animationTime = 500)
                                ); selectedElem.value = 0
                            },
                            text = { Text("Recents") }, icon = { Icon(Icons.Filled.Refresh, "New Cards") },
                            shape = CircleShape

                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Search Screen") },
                            label = { Text("Search") },
                            selected = selectedElem.value == 1,
                            onClick = {
                                navController.launch(
                                    screen = NavigationRoute.Search.name,
                                    animationType = AnimationType.Present(animationTime = 500)
                                ); selectedElem.value = 1
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Collections") },
                            label = { Text("Collections") },
                            selected = selectedElem.value == 2,
                            onClick = {
                                navController.launch(
                                    screen = NavigationRoute.Collections.name,
                                    animationType = AnimationType.Present(animationTime = 500)
                                ); selectedElem.value = 2
                            }
                        )
                    }
                }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    content()
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                val navController = LocalRootController.current
                NavigationRail(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        .padding(top = 44.dp, bottom = 56.dp)
                        .fillMaxHeight(),

                    ) {
                    FloatingActionButton(
                        onClick = {
                            navController.launch(
                                screen = NavigationRoute.NewCardViewer.name,
                                animationType = AnimationType.Present(animationTime = 500)
                            ); selectedElem.value = 0
                        }
                    ) {
                        Icon(Icons.Filled.Refresh, "New Cards")
                    }

                    Spacer(modifier = Modifier.size(20.dp))
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Search Screen") },
                        label = { Text("Search") },
                        selected = selectedElem.value == 1,
                        onClick = {
                            navController.launch(
                                screen = NavigationRoute.Search.name,
                                animationType = AnimationType.Present(animationTime = 500)
                            ); selectedElem.value = 1
                        }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Collections") },
                        label = { Text("Collections") },
                        selected = selectedElem.value == 2,
                        onClick = {
                            navController.launch(
                                screen = NavigationRoute.Collections.name,
                                animationType = AnimationType.Present(animationTime = 500)
                            ); selectedElem.value = 2
                        }
                    )
                }
                content()
            }
        }
    }
}

internal fun RootComposeBuilder.NavigationGraph() {
    val selectedElem = mutableStateOf(1)

    screen(NavigationRoute.Login.name) {
        LoginScreen()
    }

    screen(NavigationRoute.Registration.name) {
        RegistrationScreen()
    }

    screen(NavigationRoute.NewCardViewer.name) {
        SideNavigation(selectedElem = selectedElem) {
            NewCardViewerScreen()
        }
    }

    screen(NavigationRoute.CardCollectionViewer.name) {
        SideNavigation(selectedElem = selectedElem) {
            CardCollectionViewerScreen(it as UserCardCollectionDTO)
        }
    }

    screen(NavigationRoute.EditCard.name) {
        SideNavigation(selectedElem = selectedElem) {
            EditAddCardScreen(card = (it as CardDTO), isNew = false)
        }
    }

    screen(NavigationRoute.AddCard.name) {
        SideNavigation(selectedElem = selectedElem) {
            EditAddCardScreen(card = (it as CardDTO), isNew = true)
        }
    }

    screen(NavigationRoute.Search.name) {
        SideNavigation(selectedElem = selectedElem) {
            SearchScreen()
        }
    }
    screen(NavigationRoute.SearchHistory.name) {
        SideNavigation(selectedElem = selectedElem) {
            SearchHistoryScreen()
        }
    }
    screen(NavigationRoute.Definition.name) {
        SideNavigation(selectedElem = selectedElem) {
            DefinitionScreen(term = it as String)
        }
    }

    screen(NavigationRoute.Collections.name) {
        SideNavigation(selectedElem = selectedElem) {
            CollectionsScreen()
        }
    }

    screen(NavigationRoute.Game.name) {
        SideNavigation(selectedElem = selectedElem) {
            GameScreen(collectionId = it as String)
        }
    }

    screen(NavigationRoute.GameResults.name) { cardsAndResults ->
        if (cardsAndResults !is Pair<*, *>) return@screen
        SideNavigation(selectedElem = selectedElem) {
            GameResultsScreen(
                cards = (cardsAndResults.first as List<CardDTO>),
                gameResults = cardsAndResults.second as List<Boolean>
            )
        }
    }
}

internal enum class NavigationRoute {
    Login, Collections, Game, GameResults, Search, SearchHistory, Definition, EditCard, CardCollectionViewer, NewCardViewer, AddCard, Registration
}
