import androidx.compose.ui.window.ComposeUIViewController
import ua.ukma.edu.danki.App
import platform.UIKit.UIViewController

fun MainViewController(systemAppearance: (isLight: Boolean) -> Unit): UIViewController {
    return ComposeUIViewController { App(systemAppearance) }
}
