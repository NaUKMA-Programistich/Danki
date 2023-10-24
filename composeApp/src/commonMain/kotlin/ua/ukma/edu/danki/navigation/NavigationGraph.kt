package ua.ukma.edu.danki.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ua.ukma.edu.danki.screens.login.LoginScreen

internal fun RootComposeBuilder.NavigationGraph() {

    screen(NavigationRoute.Login.name) {
        LoginScreen()
    }

}

internal enum class NavigationRoute {
    Login,
    Main,
    Orders,
    OrderDetails,
    Products,
    StoreProducts,
    Categories,
    Profiles,
    CustomerCars,
    Options
}
