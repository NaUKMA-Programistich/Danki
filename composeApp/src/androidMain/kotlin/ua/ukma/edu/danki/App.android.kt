package ua.ukma.edu.danki

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import io.ktor.client.*
import io.ktor.client.engine.android.*
import ru.alexgladkov.odyssey.compose.setup.OdysseyConfiguration
import ru.alexgladkov.odyssey.core.configuration.DisplayType
import ua.ukma.edu.danki.theme.mColors
import java.net.InetSocketAddress
import java.net.Proxy

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
        setContent {
            val surfaceColor = mColors.surface
            val configuration =
                OdysseyConfiguration(
                    canvas = this,
                    backgroundColor = surfaceColor,
                    displayType = DisplayType.FullScreen,
                )
            App(
                systemAppearance = {},
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