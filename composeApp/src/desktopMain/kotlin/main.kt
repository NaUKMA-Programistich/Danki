import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import java.awt.Dimension
import ua.ukma.edu.danki.App

fun main() = application {
    Window(
        title = "Danki",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)

        App(odysseyConfiguration = OdysseyConfiguration())
    }
}