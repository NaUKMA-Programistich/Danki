package ua.ukma.edu.danki.screens.card_collection_viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.ukma.edu.danki.theme.surfaceContainer

@Composable
fun SharedCodeDialog(
    sharedCode: Long,
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Share this code with your friends",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                modifier = Modifier.padding(8.dp).fillMaxWidth(0.8f),
                value = sharedCode.toString(),
                onValueChange = { },
                shape = MaterialTheme.shapes.small,
                placeholder = { },
                singleLine = true,

                )
        }
    }
}