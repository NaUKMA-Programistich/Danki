import androidx.compose.ui.window.ComposeUIViewController
import ua.ukma.edu.danki.App
import platform.UIKit.UIViewController
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ua.ukma.edu.danki.theme.mColors

fun MainViewController(systemAppearance: (isLight: Boolean) -> Unit): UIViewController {
    return ComposeUIViewController {
        val configuration = OdysseyConfiguration(
            backgroundColor = mColors.background,
        )

        App(systemAppearance, configuration)
    }
}
