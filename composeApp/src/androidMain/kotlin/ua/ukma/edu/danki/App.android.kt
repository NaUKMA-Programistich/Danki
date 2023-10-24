package ua.ukma.edu.danki

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.core.configuration.DisplayType
import ua.ukma.edu.danki.theme.mColors

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val systemBarColor = Color.TRANSPARENT
        setContent {
            val view = LocalView.current
            var isLightStatusBars by remember { mutableStateOf(false) }
            if (!view.isInEditMode) {
                // TODO Resolve issue with Odyssey not rendering
                //LaunchedEffect(isLightStatusBars) {
                    //val window = (view.context as Activity).window
                    //WindowCompat.setDecorFitsSystemWindows(window, false)
                    //window.statusBarColor = systemBarColor
                    //window.navigationBarColor = systemBarColor
                    //WindowCompat.getInsetsController(window, window.decorView).apply {
                    //    isAppearanceLightStatusBars = isLightStatusBars
                    //    isAppearanceLightNavigationBars = isLightStatusBars
                    //}
                //}
            }
            val surfaceColor = mColors.surface
            val configuration = remember {
                OdysseyConfiguration(
                    canvas = this,
                        backgroundColor = surfaceColor,
                    displayType = DisplayType.FullScreen,
                    //backgroundColor = androidx.compose.

                )
            }
            App(
                systemAppearance = { isLight -> isLightStatusBars = isLight },
                odysseyConfiguration = configuration
            )
        }
    }
}

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}